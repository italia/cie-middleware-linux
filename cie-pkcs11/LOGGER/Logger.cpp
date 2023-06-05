#include <iostream>
#include <cstdlib>
#include <ctime>
#include <iomanip>
#include <sys/stat.h>
#include <unistd.h>
#include "Logger.h"
#include <limits.h>
#include <sys/time.h>
#include <stdarg.h>

#include <regex>
using namespace std;
using namespace CieIDLogger;


inline bool config_exists(const string& name) {
	ifstream f(name.c_str());
	return f.good();
}

Logger* Logger::m_Instance = 0;

//Folder path string
//char szLogDir[PATH_MAX];

static const char* level_strings[] = {
 "", "[DEBUG]", "[INFO]", "[ERROR]"
};


Logger::Logger()
{
	char pProcessInfo[PATH_MAX];
    
	string sConfig;
    char cTime [80];
    timeval curTime;
    t_configTime = (time_t)0;

    // Initialize mutex
    int ret = 0;
    pthread_mutexattr_init(&m_Attr);
    ret = pthread_mutexattr_settype(&m_Attr, PTHREAD_MUTEX_RECURSIVE);
    if (ret != 0)
    {
        printf("Logger::Logger() -- Mutex attribute not initialize!! Ret: %d\n", ret);
        
        exit(0);
    }
    ret = pthread_mutex_init(&m_Mutex, &m_Attr);
    if (ret != 0)
    {
        printf("Logger::Logger() -- Mutex not initialize!!\n");
        exit(0);
    }
    
    char* home = getenv("HOME");
    std::string path(home);
    
    path.append("/.CIEPKI/");
    
    //check if folder exist
    struct stat st = {0};
    
    if (stat(path.c_str(), &st) == -1) {
        mkdir(path.c_str(), 0700);
    }
    
    gettimeofday(&curTime, NULL);
    strftime(cTime, sizeof(cTime), "%Y-%m-%d", gmtime(&curTime.tv_sec));
    
    sprintf(pbLog, "%s_%s.log","CIEPKI", cTime);
    path.append(pbLog);
    
    memcpy(pbLog, path.data(), path.length());
    pbLog[path.length()] = 0;

	int log_level = getLogConfig();
	
	if (log_level == LOG_STATUS_DISABLED) {
		disableLog();
	}
	else {

		m_File.open(pbLog, ios::out | ios::app);
		m_File << endl << "-----------------------------------------------------------------" << endl << endl;

		m_LogLevel = static_cast<LogLevel>(log_level);
		m_LogStatus = LOG_STATUS_ENABLED;

        /*
        char cBuf [PATH_MAX];
        uint32_t bufsize = PATH_MAX;

        if(!_NSGetExecutablePath(cBuf, &bufsize))
            sprintf(pProcessInfo, "Process: '%s'", cBuf);
        
		m_File << pProcessInfo << endl;
        */
		m_File.flush();
		m_File.close();
	}
	
	m_LogType = FILE_LOG;

}

Logger::~Logger()
{
	m_File.close();
	pthread_mutexattr_destroy(&m_Attr);
	pthread_mutex_destroy(&m_Mutex);
}

Logger* Logger::getInstance() throw ()
{

	if (m_Instance == 0)
	{
		m_Instance = new Logger();
	}

	int log_level = m_Instance->getLogConfig();
    //printf("Lib log level: %d\n", log_level);

	if (log_level == LOG_STATUS_DISABLED) {
		m_Instance->disableLog();
	}
	else if(log_level > 0 && log_level < 3){
		m_Instance->enableFileLogging();
		m_Instance->enableLog();
		m_Instance->updateLogLevel(static_cast<LogLevel>(log_level));
	}

	return m_Instance;
}


void Logger::writeConfigFile(string& filePath, string& sConfig) throw() {
	m_ConfigFile.open(filePath, ios::out);
	m_ConfigFile << sConfig;
	m_ConfigFile.close();
}

int Logger::getLogConfig() throw() {
	char pbConfig[PATH_MAX];
	string sConfig;
	int log_level;
	struct stat result;
	    
    char* home = getenv("HOME");
    std::string path(home);
	
    /*
    if(path.find("/Library") == string::npos){
        path.append("/Library/Containers/it.ipzs.CIE-ID.CIEToken/Data");
    }
    */

    path.append("/.CIEPKI/");
	
    //check if folder exist
    struct stat st = {0};
    
    if (stat(path.c_str(), &st) == -1) {
        mkdir(path.c_str(), 0700);
    }
    
    sprintf(pbConfig, "%s/config",path.data());
    
	if (!config_exists(pbConfig)) {
		sConfig = "LIB_LOG_LEVEL=2";
        string stConfig = string(pbConfig);
		writeConfigFile(stConfig, sConfig);
		t_configTime = 0;
	}
	
	volatile int stat_res = stat(pbConfig, &result);

	if (stat_res == 0) {
		if (t_configTime < result.st_mtime) {
			t_configTime = result.st_mtime;
            
            lock();
			m_ConfigFile.open(pbConfig, ios::in);
			m_ConfigFile >> sConfig;
			m_ConfigFile.close();
            unlock();
            
			sscanf(sConfig.data(), "LIB_LOG_LEVEL=%d", &log_level);
			if (log_level < 0 && log_level > 3) {
				log_level = 0;
				sConfig = "LIB_LOG_LEVEL=2";
                string stConfig = string(pbConfig);
                writeConfigFile(stConfig, sConfig);
			}

			m_LogLevel = static_cast<LogLevel>(log_level);
		}
	}

	return m_LogLevel;
}

void Logger::lock()
{
	pthread_mutex_lock(&m_Mutex);
}

void Logger::unlock()
{
	pthread_mutex_unlock(&m_Mutex);
}

void Logger::logIntoFile(string& data)
{
	lock();
	m_File << getCurrentTime() << "  " << data << endl;
	unlock();
}

void Logger::logOnConsole(string& data)
{
	cout << getCurrentTime() << "  " << data << endl;
}

string Logger::getCurrentTime()
{
	char pbtDate[256];
    timeval curTime;
    gettimeofday(&curTime, NULL);
    int milli = curTime.tv_usec / 1000;
    
    strftime(pbtDate, sizeof(pbtDate), "%Y-%m-%d %H:%M:%S", gmtime(&curTime.tv_sec));
    
    sprintf(pbtDate, "%s:%03d", pbtDate, milli);

	return pbtDate;
}


void Logger::log_log(ostream& out, LogLevel level, const char* text) throw() {
	if (m_LogStatus == LOG_STATUS_ENABLED) {

		if (level < m_LogLevel) {
			return;
		}

		string data;
		data.append(level_strings[level]);
		data.append(" ");
		data.append(text);

		lock();
		m_File.open(pbLog, ios::out | ios::app);
		m_File << getCurrentTime() << "  " << data << endl;
		m_File.flush();
		m_File.close();
		unlock();

	}
}

// Interface for Debug Log
void Logger::debug(const char* fmt, ...) throw()
{
	char buffer[4096];
	va_list args;
	va_start(args, fmt);

	vsprintf(buffer, fmt, args);
	va_end(args);

	switch (m_LogType)
	{
	case FILE_LOG:
		log_log(m_File, LOG_LEVEL_DEBUG, buffer);
		break;
	case CONSOLE:
		log_log(cout, LOG_LEVEL_DEBUG, buffer);
	default:
		break;
	}
}

void Logger::debug(string& text) throw()
{
	debug(text.data());
}

void Logger::debug(ostringstream& stream) throw()
{
	string text = stream.str();
	debug(text.data());
}


// Interface for Info Log
void Logger::info(const char* fmt, ...) throw()
{
	char buffer[1024];
	va_list args;
	va_start(args, fmt);

	vsprintf(buffer, fmt, args);
	va_end(args);

	switch (m_LogType)
	{
	case FILE_LOG:
		log_log(m_File, LOG_LEVEL_INFO, buffer);
		break;
	case CONSOLE:
		log_log(cout, LOG_LEVEL_INFO, buffer);
	default:
		break;
	}
}

void Logger::info(string& text) throw()
{
	info(text.data());
}

void Logger::info(ostringstream& stream) throw()
{
	string text = stream.str();
	info(text.data());
}


// Interface for Error Log
int Logger::error(const char* fmt, ...) throw()
{
	char buffer[1024];
	va_list args;
	va_start(args, fmt);

	vsprintf(buffer, fmt, args);
	va_end(args);

	switch (m_LogType)
	{
	case FILE_LOG:
		log_log(m_File, LOG_LEVEL_ERROR, buffer);
		break;
	case CONSOLE:
		log_log(cout, LOG_LEVEL_ERROR, buffer);
	default:
		break;
	}

	return -1;
}

int Logger::error(string& text) throw()
{
	return error(text.data());
}

int Logger::error(ostringstream& stream) throw()
{
	string text = stream.str();
	return error(text.data());
}


// Interface for Buffer Log 
void Logger::buffer(uint8_t* buff, size_t buff_size) throw()
{
	if (m_LogLevel == LOG_LEVEL_DEBUG) {
		switch (m_LogType)
		{
		case FILE_LOG:
			print_bytes(m_File, buff, buff_size, true);
			break;
		case CONSOLE:
			print_bytes(cout, buff, buff_size, true);
		default:
			break;
		}
	}
}

void Logger::print_bytes(ostream& out, uint8_t* data, size_t dataLen, bool format) {
	size_t index = 0;


	lock();
	m_File.open(pbLog, ios::out | ios::app);

	m_File << setfill('0');
	m_File << endl;

	m_File << "0x" << hex << setw(8) << index << "\t";

	for (size_t index = 0; index < dataLen; index++) {

		if (index) {
			if ((index % 16) == 0) {
				m_File << "\n0x" << hex << setw(8) << index << "\t";
			}
			else if ((index % 8) == 0) {
				m_File << " -  ";
			}
		}

		m_File << hex << setw(2) << (int)data[index] << " ";
	}
	m_File << endl << endl;


	m_File.close();
	unlock();
}

#if 0
void Logger::buffer(string& text) throw()
{
	buffer(text.data());
}

void Logger::buffer(ostringstream& stream) throw()
{
	string text = stream.str();
	buffer(text.data());
}
#endif

void Logger::updateLogLevel(LogLevel logLevel)
{
	m_LogLevel = logLevel;
}

void Logger::enableLog()
{
	m_LogStatus = LOG_STATUS_ENABLED;
}

void Logger::disableLog()
{
	m_LogStatus = LOG_STATUS_DISABLED;
}

void Logger::updateLogType(LogType logType)
{
	m_LogType = logType;
}

void Logger::enableConsoleLogging()
{
	m_LogType = CONSOLE;
}

void Logger::enableFileLogging()
{
	m_LogType = FILE_LOG;
}

