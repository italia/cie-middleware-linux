
#include "TLV.h"

//extern CLog Log;

CTLV::CTLV(ByteArray &data)
{
	init_func
	uint32_t dwPtr=0;
	while (dwPtr<data.size()) {
		uint8_t btLen=data[dwPtr+1];
		if (btLen<255) {
			if (dwPtr + btLen + 2 > data.size()) 
				return;
			map[data[dwPtr]]=ByteArray(data.mid(dwPtr,btLen+2));
			dwPtr+=btLen+2;
		}
		else {
			uint32_t dwLen = ByteArrayToVar(data.mid(dwPtr + 2),uint32_t);
			if (dwPtr + dwLen + 2 + sizeof(uint32_t) > data.size())
				return;
			map[data[dwPtr]] = ByteArray(data.mid(dwPtr, dwLen + 2 + sizeof(uint32_t)));
			dwPtr += dwLen + 2 + sizeof(uint32_t);
		}
	}
	exit_func
}

CTLV::~CTLV(void)
{
}

ByteArray *CTLV::getTAG(uint8_t Tag)
{
	init_func
	tlvMap::iterator it=map.find(Tag);
	if (it!=map.end())
		return &it->second;
	else
		return nullptr;
}

ByteArray CTLV::getValue(uint8_t Tag)
{
	init_func
	tlvMap::iterator it=map.find(Tag);
	if (it != map.end()) {
		// Due errori in una riga sola: 0x255 vale 597 e it->second[0] è un
		// uint8_t, quindi il confronto era sempre vero e il ramo else era
		// codice morto; inoltre l'indice 0 è il tag, non la lunghezza.
		// Il costruttore qui sopra usa data[dwPtr+1] con soglia 255 e mette il
		// valore a offset 2 (forma corta) oppure 2 + sizeof(uint32_t) (forma
		// lunga): qui va replicata la stessa logica.
		if (it->second[1] < 255)
			return it->second.mid(2);
		else
			return it->second.mid(6);
	}
	else
		return ByteArray();
}

CTLVCreate::CTLVCreate()
{
}

CTLVCreate::~CTLVCreate(void)
{
}

ByteDynArray * CTLVCreate::getValue(uint8_t Tag)
{
	init_func
	tlvCreateMap::iterator it=map.find(Tag);
	if (it!=map.end())
		return &it->second;
	else
		return nullptr;
}

ByteDynArray * CTLVCreate::addValue(uint8_t Tag)
{
	init_func
	map[Tag].clear();
	return &map[Tag];
}

void CTLVCreate::setValue(uint8_t Tag,ByteArray &Value)
{
	init_func
	map[Tag] = Value;
}

ByteDynArray CTLVCreate::getBuffer()
{
	init_func
	uint32_t dwSize=0;
	tlvCreateMap::iterator it=map.begin();
	while (it!=map.end()) {
		if (it->second.size() < 0xff)
			dwSize += (uint32_t)it->second.size() + 2;
		else
			dwSize += (uint32_t)it->second.size() + 2 + sizeof(uint32_t);
		it++;
	}
	ByteDynArray Value(dwSize);
	uint32_t dwPtr = 0;
	it=map.begin();
	while (it!=map.end()) {
		Value[dwPtr]=it->first;
		dwPtr++;
		if (it->second.size()<0xff) {
			Value[dwPtr] = (uint8_t)it->second.size();
			dwPtr++;
		}
		else {
			Value[dwPtr]=0xff;
			dwPtr++;
			uint32_t dwSize = (uint32_t)it->second.size();
			Value.copy(VarToByteArray(dwSize), dwPtr);
			dwPtr += sizeof(uint32_t);
		}

		Value.copy(it->second, dwPtr);
		dwPtr += (uint32_t)it->second.size();
		it++;
	}
	return Value;
}
