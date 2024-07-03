//
//  AbilitaCIE.cpp
//  cie-pkcs11
//
//  Created by ugo chirico on 02/09/18.
//  Copyright © 2018 IPZS. All rights reserved.
//

#include <stdio.h>
#include <Foundation/Foundation.h>

#include<stdio.h>    //printf
#include<string.h>    //strlen
#include<sys/socket.h>    //socket
#include<arpa/inet.h>    //inet_addr

#include "../Crypto/CryptoUtil.h"

#include <AppKit/AppKit.h>

using namespace CryptoPP;

void showUI(const char* szPAN)
{
    NSTask *task = [[NSTask alloc] init];

    task.launchPath = @"/usr/bin/open";
    task.arguments = @[@"-n", @"/Applications/CIE ID.app"];//, [NSString stringWithUTF8String:szPAN]];

    [task launch];
}

int sendMessage(const char* szCommand, const char* szParam)
{
    int sock;
    struct sockaddr_in server;
    char szMessage[100] , szServerReply[1000];
    
    //Create socket
    sock = socket(AF_INET , SOCK_STREAM , 0);
    if (sock == -1)
    {
        printf("Could not create socket");
    }
    puts("Socket created");
    
    server.sin_addr.s_addr = inet_addr("127.0.0.1");
    server.sin_family = AF_INET;
    server.sin_port = htons( 88888 );
    
    //Connect to remote server
    if (connect(sock , (struct sockaddr *)&server , sizeof(server)) < 0)
    {
        perror("connect failed. Error");
        return 1;
    }
    
    puts("Connected\n");
    
    if(szParam)
        sprintf(szMessage, "%s:%s", szCommand, szParam);
    else
        sprintf(szMessage, "%s", szCommand);
    
    std::string sMessage = szMessage;
    std::string sCipherText;
    
    encrypt(sMessage, sCipherText);
    
    int messagelen = (int)sCipherText.size();
    std::string sHeader((char*)&messagelen, sizeof(messagelen));
    
    sMessage = sHeader.append(sCipherText);
    
    //Send some data
    if( send(sock , sMessage.c_str(), (size_t)sMessage.length() , 0) < 0)
    {
        puts("Send failed");
        return 2;
    }
    
    //Receive a reply from the server
    if( recv(sock , szServerReply , 100 , 0) < 0)
    {
        puts("recv failed");
        return 3;
    }
    
    puts("Server reply :");
    puts(szServerReply);
    
    close(sock);
    return 0;
}

void notifyPINLocked()
{
    sendMessage("pinlocked", NULL);
}

void notifyPINWrong(int trials)
{
    char szParam[100];
    sprintf(szParam, "%d", trials);
    
    sendMessage("pinwrong", szParam);
}

void notifyCardNotRegistered(const char* szPAN)
{
    sendMessage("cardnotregistered", szPAN);        
}


