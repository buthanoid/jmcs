/*******************************************************************************
* JMMC project
*
* "@(#) $Id: miscTestDynBuf.c,v 1.15 2005-02-10 23:51:54 lafrasse Exp $"
*
* who       when         what
* --------  -----------  -------------------------------------------------------
* lafrasse  23-Jun-2004  Created
* lafrasse  09-Jul-2004  Passed some polish
* lafrasse  20-Jul-2004  Removed all '\0' from char arrays
* lafrasse  23-Jul-2004  Added error management to
*                        miscDynBufGetStoredBytesNumber and
*                        miscDynBufGetAllocatedBytesNumber, plus
*                        miscDynBufGetBytesFromTo parameter refinments
* lafrasse  02-Aug-2004  Added miscTesDynStr code, due to null-terminated
*                        string specific functions move from miscDynStr.h to
*                        miscDynBuf.h
* lafrasse  23-Aug-2004  Moved miscDynBufInit from local to public
* lafrasse  08-Nov-2004  Added miscDynBufGetNextLinePointer(),
*                        miscDynBufGetCommentPattern(),
*                        miscDynBufSetCommentPattern() and miscDynBufLoadFile()
*                        functions test
* gzins     21-Dec-2004  Renamed miscDynBufGetStoredBytesNumber to
*                        miscDynBufGetNbStoredBytes and
*                        miscDynBufGetAllocatedBytesNumber to
*                        miscDynBufGetNbAllocatedBytes
* lafrasse  23-Jun-2004  Added miscDynBufSaveInFile() test
*
*******************************************************************************/

static char *rcsId="@(#) $Id: miscTestDynBuf.c,v 1.15 2005-02-10 23:51:54 lafrasse Exp $"; 
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

/* 
 * System Headers 
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>


/*
 * MCS Headers 
 */
#include "mcs.h"
#include "err.h"


/*
 * Local Headers 
 */
#include "miscDynBuf.h"


/*
 * Local Functionss 
 */
void displayExecStatus(mcsCOMPL_STAT);
void displayDynBuf(miscDYN_BUF*);


/* 
 * Main
 */

#define SUCCEED "SUCCEED"
#define FAILED "FAILED"

int main (int argc, char *argv[])
{
    /* Give process name to mcs library */
    mcsInit(argv[0]);

    mcsCOMPL_STAT  executionStatusCode;

    miscDYN_BUF    dynBuf;

    char           *bytes       = NULL;
    int            bytesNumber  = 0;

    char           byte         = '\0';

    mcsUINT32      storedBytes  = 0;

    mcsUINT32      position     = 0;
    mcsUINT32      from         = 0;
    mcsUINT32      to           = 0;



    /* miscDynBufInit */
    printf("---------------------------------------------------------------\n");
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    displayDynBuf(NULL);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    miscDynBufInit(&dynBuf);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");



    /* miscDynBufAppendBytes */
    printf("---------------------------------------------------------------\n");
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    printf("miscDynBufAppendBytes(&dynBuf = NULL) : ");
    executionStatusCode = miscDynBufAppendBytes(NULL, bytes, bytesNumber);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    printf("miscDynBufAppendBytes(NULL, 0) ");
    executionStatusCode = miscDynBufAppendBytes(&dynBuf, bytes, bytesNumber);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    bytes = "hello dynBuf" ;
    bytesNumber = strlen(bytes);
    printf("miscDynBufAppendBytes(\"%s\", %d) ", bytes, bytesNumber);
    executionStatusCode = miscDynBufAppendBytes(&dynBuf, bytes, bytesNumber);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    displayDynBuf(&dynBuf);
    printf("\n");

    bytes    = " ... :)" ;
    bytesNumber = strlen(bytes);
    printf("miscDynBufAppendBytes(\"%s\", %d) ", bytes, bytesNumber);
    executionStatusCode = miscDynBufAppendBytes(&dynBuf, bytes, bytesNumber);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    displayDynBuf(&dynBuf);
    printf("\n");

    bytes    = " !!!" ;
    bytesNumber = strlen(bytes);
    printf("miscDynBufAppendBytes(\"%s\", %d) ", bytes, bytesNumber);
    executionStatusCode = miscDynBufAppendBytes(&dynBuf, bytes, bytesNumber);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    displayDynBuf(&dynBuf);
    printf("\n");



    /* miscDynBufGetByteAt */
    printf("---------------------------------------------------------------\n");
    position = miscDYN_BUF_BEGINNING_POSITION - 1;
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    executionStatusCode = miscDynBufGetByteAt(NULL, NULL, position);
    printf("miscDynBufGetByteAt(NULL, %d) ", position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    executionStatusCode = miscDynBufGetByteAt(&dynBuf, NULL, position);
    printf("miscDynBufGetByteAt(NULL, %d) ", position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    executionStatusCode = miscDynBufGetByteAt(&dynBuf, &byte, position);
    printf("miscDynBufGetByteAt(%d) ", position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    position = miscDYN_BUF_BEGINNING_POSITION;
    executionStatusCode = miscDynBufGetByteAt(&dynBuf, &byte, position);
    printf("miscDynBufGetByteAt(%d) = \"%c\" ", position, byte);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    position = 7;
    executionStatusCode = miscDynBufGetByteAt(&dynBuf, &byte, position);
    printf("miscDynBufGetByteAt(%d) = \"%c\" ", position, byte);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    miscDynBufGetNbStoredBytes(&dynBuf, &position);
    executionStatusCode = miscDynBufGetByteAt(&dynBuf, &byte, position);
    printf("miscDynBufGetByteAt(%d) = \"%c\" ", position, byte);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    miscDynBufGetNbStoredBytes(&dynBuf, &position);
    position += 1;
    executionStatusCode = miscDynBufGetByteAt(&dynBuf, &byte, position);
    printf("miscDynBufGetByteAt(%d) ", position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");



    /* miscDynBufGetBytesFromTo */
    printf("---------------------------------------------------------------\n");
    from = miscDYN_BUF_BEGINNING_POSITION - 1;
    to = 4;
    miscDynBufGetNbStoredBytes(&dynBuf, &storedBytes);
    bytes = calloc(sizeof(char), storedBytes + 1);
    bytes[0] = '\0';
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    executionStatusCode = miscDynBufGetBytesFromTo(NULL, bytes, from, to);
    printf("miscDynBufGetBytesFromTo(%d, %d) ", from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    executionStatusCode = miscDynBufGetBytesFromTo(&dynBuf, bytes, from, to);
    printf("miscDynBufGetBytesFromTo(%d, %d) ", from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    from = miscDYN_BUF_BEGINNING_POSITION;
    to = 12;
    executionStatusCode = miscDynBufGetBytesFromTo(&dynBuf, NULL, from, to);
    printf("miscDynBufGetBytesFromTo(NULL, %d, %d) ", from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    executionStatusCode = miscDynBufGetBytesFromTo(&dynBuf, bytes, from, to);
    bytes[(to - from) + 1] = '\0';
    printf("miscDynBufGetBytesFromTo(%d, %d) = \"%s\" ", from, to, bytes);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    bytes[0] = '\0';
    printf("\n");

    from = 7;
    to = 16;
    bytes[(to - from) + 1] = '\0';
    executionStatusCode = miscDynBufGetBytesFromTo(&dynBuf, bytes, from, to);
    printf("miscDynBufGetBytesFromTo(%d, %d) = \"%s\" ", from, to, bytes);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    bytes[0] = '\0';
    printf("\n");

    from = 6;
    to = 6;
    bytes[(to - from) + 1] = '\0';
    executionStatusCode = miscDynBufGetBytesFromTo(&dynBuf, bytes, from, to);
    printf("miscDynBufGetBytesFromTo(%d, %d) = \"%s\" ", from, to, bytes);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    bytes[0] = '\0';
    printf("\n");

    from = 18;
    miscDynBufGetNbStoredBytes(&dynBuf, &to);
    executionStatusCode = miscDynBufGetBytesFromTo(&dynBuf, bytes, to, from);
    printf("miscDynBufGetBytesFromTo(%d, %d) ", to, from);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    bytes[0] = '\0';
    printf("\n");

    executionStatusCode = miscDynBufGetBytesFromTo(&dynBuf, bytes, from, to);
    bytes[(to - from) + 1] = '\0';
    printf("miscDynBufGetBytesFromTo(%d, %d) = \"%s\" ", from, to, bytes);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    bytes[0] = '\0';
    printf("\n");

    miscDynBufGetNbStoredBytes(&dynBuf, &to);
    to += 1;
    executionStatusCode = miscDynBufGetBytesFromTo(&dynBuf, bytes, from, to);
    printf("miscDynBufGetBytesFromTo(%d, %d) ", from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    free(bytes);
    printf("\n");



    /* miscDynBufReplaceByteAt */
    printf("---------------------------------------------------------------\n");
    position = miscDYN_BUF_BEGINNING_POSITION - 1;
    byte = 'H';
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    executionStatusCode = miscDynBufReplaceByteAt(NULL, byte, position);
    printf("miscDynBufReplaceByteAt(%d) ", position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    executionStatusCode = miscDynBufReplaceByteAt(&dynBuf, byte, position);
    printf("miscDynBufReplaceByteAt(%d) ", position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    position = miscDYN_BUF_BEGINNING_POSITION;
    executionStatusCode = miscDynBufReplaceByteAt(&dynBuf, byte, position);
    printf("miscDynBufReplaceByteAt(%d) = \"%c\" ", position, byte);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    position = 7;
    byte = 'B';
    executionStatusCode = miscDynBufReplaceByteAt(&dynBuf, byte, position);
    printf("miscDynBufReplaceByteAt(%d) = \"%c\" ", position, byte);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    position = 13;
    byte = '\'';
    executionStatusCode = miscDynBufReplaceByteAt(&dynBuf, byte, position);
    printf("miscDynBufReplaceByteAt(%d) = \"%c\" ", position, byte);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    miscDynBufGetNbStoredBytes(&dynBuf, &position);
    byte = '@';
    executionStatusCode = miscDynBufReplaceByteAt(&dynBuf, byte, position);
    printf("miscDynBufReplaceByteAt(%d) = \"%c\" ", position, byte);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    miscDynBufGetNbStoredBytes(&dynBuf, &position);
    position += 1;
    executionStatusCode = miscDynBufReplaceByteAt(&dynBuf, byte, position);
    printf("miscDynBufReplaceByteAt(%d) ", position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");



    /* miscDynBufInsertBytesAt */
    printf("---------------------------------------------------------------\n");
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    executionStatusCode = miscDynBufInsertBytesAt(NULL, NULL, 0, 0);
    printf("miscDynBufInsertBytesAt(NULL, 0, 0) ");
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    position = miscDYN_BUF_BEGINNING_POSITION - 1;
    executionStatusCode = miscDynBufInsertBytesAt(&dynBuf, NULL, 0, position);
    printf("miscDynBufInsertBytesAt(NULL, 0, %d) ", position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    position = miscDYN_BUF_BEGINNING_POSITION;
    executionStatusCode = miscDynBufInsertBytesAt(&dynBuf, NULL, 0, position);
    printf("miscDynBufInsertBytesAt(NULL, 0, %d) ", position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    bytes = "Encore un '";
    bytesNumber = 0;
    printf("miscDynBufInsertBytesAt(\"%s\", %d, %d) ", bytes, bytesNumber,
           position);
    executionStatusCode = miscDynBufInsertBytesAt(&dynBuf, bytes, bytesNumber,
                                                  position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    bytesNumber = strlen(bytes);
    executionStatusCode = miscDynBufInsertBytesAt(&dynBuf, bytes, bytesNumber,
                                                  position);
    printf("miscDynBufInsertBytesAt(\"%s\", %d, %d) ", bytes, bytesNumber,
           position);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    position = 18;
    bytes = "misc";
    bytesNumber = 0;
    executionStatusCode = miscDynBufInsertBytesAt(&dynBuf, bytes, bytesNumber,
                                                  position);
    printf("miscDynBufInsertBytesAt(\"%s\", %d, %d) ", bytes, bytesNumber,
           position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    bytesNumber = strlen(bytes);
    executionStatusCode = miscDynBufInsertBytesAt(&dynBuf, bytes, bytesNumber,
                                                  position);
    printf("miscDynBufInsertBytesAt(\"%s\", %d, %d) ", bytes, bytesNumber,
           position);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    miscDynBufGetNbStoredBytes(&dynBuf, &position);
    bytes = "~~~";
    bytesNumber = 0;
    executionStatusCode = miscDynBufInsertBytesAt(&dynBuf, bytes, bytesNumber,
                                                  position);
    printf("miscDynBufInsertBytesAt(\"%s\", %d, %d) ", bytes, 0,
           position);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    bytesNumber = strlen(bytes);
    executionStatusCode = miscDynBufInsertBytesAt(&dynBuf, bytes, bytesNumber,
                                                  position);
    printf("miscDynBufInsertBytesAt(\"%s\", %d, %d) ", bytes, bytesNumber,
           position);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    miscDynBufGetNbStoredBytes(&dynBuf, &position);
    position += 1;
    executionStatusCode = miscDynBufInsertBytesAt(&dynBuf, bytes, bytesNumber,
                                                  position);
    printf("miscDynBufInsertBytesAt(\"%s\", %d, %d) ", bytes, bytesNumber,
           position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");



    /* miscDynBufReplaceBytesFromTo */
    printf("---------------------------------------------------------------\n");
    from = miscDYN_BUF_BEGINNING_POSITION - 1;
    to = 9;
    bytes = NULL;
    bytesNumber = 0;
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    printf("miscDynBufReplaceBytesFromTo(\"%s\", %d, %d, %d) ", bytes,
           bytesNumber, from, to);
    executionStatusCode = miscDynBufReplaceBytesFromTo(NULL, bytes, bytesNumber, 
                                                       from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    printf("miscDynBufReplaceBytesFromTo(\"%s\", %d, %d, %d) ", bytes,
           bytesNumber, from, to);
    executionStatusCode = miscDynBufReplaceBytesFromTo(&dynBuf, bytes,
                                                       bytesNumber, from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    from = miscDYN_BUF_BEGINNING_POSITION;
    bytes = "Toujours ce";
    bytesNumber = strlen(bytes);
    printf("miscDynBufReplaceBytesFromTo(\"%s\", %d, %d, %d) ", bytes,
           bytesNumber, from, to);
    executionStatusCode = miscDynBufReplaceBytesFromTo(&dynBuf, bytes,
                                                       bytesNumber, from, to);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    from = 40;
    to = 42;
    bytes = "X";
    bytesNumber = 0;
    printf("miscDynBufReplaceBytesFromTo(\"%s\", %d, %d, %d) ", bytes,
           bytesNumber, to, from);
    executionStatusCode = miscDynBufReplaceBytesFromTo(&dynBuf, bytes, bytesNumber, to,
                                                       from);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("miscDynBufReplaceBytesFromTo(\"%s\", %d, %d, %d) ", bytes,
           bytesNumber, from, to);
    executionStatusCode = miscDynBufReplaceBytesFromTo(&dynBuf, bytes,
                                                       bytesNumber, from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    bytesNumber = strlen(bytes);
    to = 40;
    printf("miscDynBufReplaceBytesFromTo(\"%s\", %d, %d, %d) ", bytes,
           bytesNumber, from, to);
    executionStatusCode = miscDynBufReplaceBytesFromTo(&dynBuf, bytes,
                                                       bytesNumber, from, to);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    from = 31;
    miscDynBufGetNbStoredBytes(&dynBuf, &to);
    bytes = " !";
    bytesNumber = strlen(bytes) + 1;
    printf("miscDynBufReplaceBytesFromTo(\"%s\", %d, %d, %d) ", bytes,
           bytesNumber, from, to);
    executionStatusCode = miscDynBufReplaceBytesFromTo(&dynBuf, bytes,
                                                       bytesNumber, from, to);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    from = 31;
    miscDynBufGetNbStoredBytes(&dynBuf, &to);
    to += 1;
    printf("miscDynBufReplaceBytesFromTo(\"%s\", %d, %d, %d) ", bytes,
           bytesNumber, from, to);
    executionStatusCode = miscDynBufReplaceBytesFromTo(&dynBuf, bytes,
                                                       bytesNumber, from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");



    /* miscDynBufDeleteBytesFromTo */
    printf("---------------------------------------------------------------\n");
    from = miscDYN_BUF_BEGINNING_POSITION - 1;
    to = 13;
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    printf("miscDynBufDeleteBytesFromTo(%d, %d) ", from, to);
    executionStatusCode = miscDynBufDeleteBytesFromTo(NULL, from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    printf("miscDynBufDeleteBytesFromTo(%d, %d) ", from, to);
    executionStatusCode = miscDynBufDeleteBytesFromTo(&dynBuf, from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    from = miscDYN_BUF_BEGINNING_POSITION;
    printf("miscDynBufDeleteBytesFromTo(%d, %d) ", from, to);
    executionStatusCode = miscDynBufDeleteBytesFromTo(&dynBuf, from, to);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    from = 7;
    to = 10;
    printf("miscDynBufDeleteBytesFromTo(%d, %d) ", to, from);
    executionStatusCode = miscDynBufDeleteBytesFromTo(&dynBuf, to, from);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("miscDynBufDeleteBytesFromTo(%d, %d) ", from, to);
    executionStatusCode = miscDynBufDeleteBytesFromTo(&dynBuf, from, to);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    from = 13;
    to = 13;
    printf("miscDynBufDeleteBytesFromTo(%d, %d) ", from, to);
    executionStatusCode = miscDynBufDeleteBytesFromTo(&dynBuf, from, to);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    from = 13;
    miscDynBufGetNbStoredBytes(&dynBuf, &to);
    to -= 1;
    printf("miscDynBufDeleteBytesFromTo(%d, %d) ", from, to);
    executionStatusCode = miscDynBufDeleteBytesFromTo(&dynBuf, from, to);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    from = 12;
    miscDynBufGetNbStoredBytes(&dynBuf, &to);
    to += 1;
    printf("miscDynBufDeleteBytesFromTo(%d, %d) ", from, to);
    executionStatusCode = miscDynBufDeleteBytesFromTo(&dynBuf, from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");



    /* miscDynBufStrip */
    printf("---------------------------------------------------------------\n");
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    printf("miscDynBufStrip() ");
    executionStatusCode = miscDynBufStrip(NULL);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    printf("miscDynBufStrip() ");
    executionStatusCode = miscDynBufStrip(&dynBuf);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");



    /* miscDynBufReset */
    printf("---------------------------------------------------------------\n");
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    printf("miscDynBufReset() ");
    executionStatusCode = miscDynBufReset(NULL);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    printf("miscDynBufReset() ");
    executionStatusCode = miscDynBufReset(&dynBuf);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");



    /* miscDynBufAppendString */
    printf("---------------------------------------------------------------\n");
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    bytes = NULL;
    printf("miscDynBufAppendString(&dynBuf = NULL) : ");
    executionStatusCode = miscDynBufAppendString(NULL, bytes);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    printf("miscDynBufAppendString(NULL) ");
    executionStatusCode = miscDynBufAppendString(&dynBuf, bytes);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    bytes = "hello dynStr" ;
    printf("miscDynBufAppendString(\"%s\") ", bytes);
    executionStatusCode = miscDynBufAppendString(&dynBuf, bytes);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    displayDynBuf(&dynBuf);
    printf("\n");

    bytes    = " ... :)" ;
    printf("miscDynBufAppendString(\"%s\") ", bytes);
    executionStatusCode = miscDynBufAppendString(&dynBuf, bytes);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    displayDynBuf(&dynBuf);
    printf("\n");

    bytes    = " !!!" ;
    printf("miscDynBufAppendString(\"%s\") ", bytes);
    executionStatusCode = miscDynBufAppendString(&dynBuf, bytes);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    displayDynBuf(&dynBuf);
    printf("\n");



    /* miscDynBufInsertStringAt */
    printf("---------------------------------------------------------------\n");
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    executionStatusCode = miscDynBufInsertStringAt(NULL, NULL, 0);
    printf("miscDynBufInsertStringAt(NULL, 0) ");
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    position = miscDYN_BUF_BEGINNING_POSITION - 1;
    executionStatusCode = miscDynBufInsertStringAt(&dynBuf, NULL, position);
    printf("miscDynBufInsertStringAt(NULL, %d) ", position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    position = miscDYN_BUF_BEGINNING_POSITION;
    executionStatusCode = miscDynBufInsertStringAt(&dynBuf, NULL, position);
    printf("miscDynBufInsertStringAt(NULL, %d) ", position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    bytes = "Encore un '";
    executionStatusCode = miscDynBufInsertStringAt(&dynBuf, bytes, position);
    printf("miscDynBufInsertStringAt(\"%s\", %d) ", bytes, position);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    position = 18;
    bytes = "misc";
    executionStatusCode = miscDynBufInsertStringAt(&dynBuf, bytes, position);
    printf("miscDynBufInsertStringAt(\"%s\", %d) ", bytes, position);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    miscDynBufGetNbStoredBytes(&dynBuf, &position);
    bytes = "~~~";
    executionStatusCode = miscDynBufInsertStringAt(&dynBuf, bytes, position);
    printf("miscDynBufInsertStringAt(\"%s\", %d) ", bytes, position);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    miscDynBufGetNbStoredBytes(&dynBuf, &position);
    position += 1;
    executionStatusCode = miscDynBufInsertStringAt(&dynBuf, bytes, position);
    printf("miscDynBufInsertStringAt(\"%s\", %d) ", bytes, position);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");



    /* miscDynBufReplaceStringFromTo */
    printf("---------------------------------------------------------------\n");
    from = miscDYN_BUF_BEGINNING_POSITION - 1;
    to = 9;
    bytes = NULL;
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    printf("miscDynBufReplaceStringFromTo(\"%s\", %d, %d) ", bytes, from, to);
    executionStatusCode = miscDynBufReplaceStringFromTo(NULL, bytes, from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    printf("miscDynBufReplaceStringFromTo(\"%s\", %d, %d) ", bytes, from, to);
    executionStatusCode = miscDynBufReplaceStringFromTo(&dynBuf, bytes, from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    from = miscDYN_BUF_BEGINNING_POSITION;
    bytes = "Toujours ce";
    printf("miscDynBufReplaceStringFromTo(\"%s\", %d, %d) ", bytes, from, to);
    executionStatusCode = miscDynBufReplaceStringFromTo(&dynBuf, bytes, from, to);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    from = 40;
    to = 42;
    bytes = " !";
    printf("miscDynBufReplaceStringFromTo(\"%s\", %d, %d) ", bytes, to, from);
    executionStatusCode = miscDynBufReplaceStringFromTo(&dynBuf, bytes, to, from);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("miscDynBufReplaceStringFromTo(\"%s\", %d, %d) ", bytes, from, to);
    executionStatusCode = miscDynBufReplaceStringFromTo(&dynBuf, bytes, from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    from = 31;
    miscDynBufGetNbStoredBytes(&dynBuf, &to);
    printf("miscDynBufReplaceStringFromTo(\"%s\", %d, %d) ", bytes, from, to);
    executionStatusCode = miscDynBufReplaceStringFromTo(&dynBuf, bytes, from, to);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");

    from = 31;
    miscDynBufGetNbStoredBytes(&dynBuf, &to);
    to += 1;
    printf("miscDynBufReplaceStringFromTo(\"%s\", %d, %d) ", bytes, from, to);
    executionStatusCode = miscDynBufReplaceStringFromTo(&dynBuf, bytes, from, to);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");



    /* miscDynBufDestroy */
    printf("---------------------------------------------------------------\n");
    printf("&dynBuf = NULL :\n");
    printf("----------------\n");
    printf("miscDynBufDestroy() ");
    executionStatusCode = miscDynBufDestroy(NULL);
    displayExecStatus(executionStatusCode);
    errCloseStack();
    printf("\n");

    printf("dynBuf Allocated :\n");
    printf("------------------\n");
    printf("miscDynBufDestroy() ");
    executionStatusCode = miscDynBufDestroy(&dynBuf);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    errCloseStack();
    printf("\n");



    /* miscDynBufSetCommentPattern */
    printf("---------------------------------------------------------------\n");
    bytes = "#";
    printf("miscDynBufSetCommentPattern(%s) ", bytes);
    executionStatusCode = miscDynBufSetCommentPattern(&dynBuf, bytes);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    printf("------------------\n");
    bytes = "//";
    printf("miscDynBufSetCommentPattern(%s) ", bytes);
    executionStatusCode = miscDynBufSetCommentPattern(&dynBuf, bytes);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    printf("------------------\n");
    bytes = "/**";
    printf("miscDynBufSetCommentPattern(%s) ", bytes);
    executionStatusCode = miscDynBufSetCommentPattern(&dynBuf, bytes);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    printf("------------------\n");
    bytes = ";--;";
    printf("miscDynBufSetCommentPattern(%s) ", bytes);
    executionStatusCode = miscDynBufSetCommentPattern(&dynBuf, bytes);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    printf("------------------\n");
    bytes = NULL;
    printf("miscDynBufSetCommentPattern(%s) ", bytes);
    executionStatusCode = miscDynBufSetCommentPattern(&dynBuf, bytes);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    printf("\n");

    /* miscDynBufLoadFile */
    printf("---------------------------------------------------------------\n");
    bytes = "../config/";
    printf("miscDynBufLoadFile(%s) ", bytes);
    executionStatusCode = miscDynBufLoadFile(&dynBuf, bytes, NULL);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    bytes = "../config/doxyfile";
    printf("miscDynBufLoadFile(%s) ", bytes);
    executionStatusCode = miscDynBufLoadFile(&dynBuf, bytes, NULL);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    printf("\n");

    /* miscDynBufSaveInFile */
    printf("---------------------------------------------------------------\n");
    bytes = "../tmp/";
    printf("miscDynBufSaveInFile(%s) ", bytes);
    executionStatusCode = miscDynBufSaveInFile(&dynBuf, bytes);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    bytes = "../tmp/test.txt";
    printf("miscDynBufSaveInFile(%s) ", bytes);
    executionStatusCode = miscDynBufSaveInFile(&dynBuf, bytes);
    displayExecStatus(executionStatusCode);
    displayDynBuf(&dynBuf);
    printf("\n");

    /* miscDynBufGetNextLinePointer */
    printf("---------------------------------------------------------------\n");
    mcsLOGICAL skipFlag;
    printf("------------------\n");
    bytes = NULL;
    skipFlag = mcsFALSE;
    miscDynBufSetCommentPattern(&dynBuf, "\0");
    printf("skipFlag = '%s Comment Skiping' | commentPattern = '%s'\n",
           (skipFlag == mcsFALSE?"WITHOUT":"WITH"),
           miscDynBufGetCommentPattern(&dynBuf));
    while ((bytes = miscDynBufGetNextLinePointer(&dynBuf, bytes, skipFlag))
           != NULL)
    {
        printf("------------------\n");
        printf("miscDynBufGetNextLinePointer() = '%s'\n", bytes);
    }
    printf("------------------\n");
    bytes = NULL;
    skipFlag = mcsFALSE;
    miscDynBufSetCommentPattern(&dynBuf, "#");
    printf("skipFlag = '%s Comment Skiping' | commentPattern = '%s'\n",
           (skipFlag == mcsFALSE?"WITHOUT":"WITH"),
           miscDynBufGetCommentPattern(&dynBuf));
    while ((bytes = miscDynBufGetNextLinePointer(&dynBuf, bytes, skipFlag))
           != NULL)
    {
        printf("------------------\n");
        printf("miscDynBufGetNextLinePointer() = '%s'\n", bytes);
    }
    printf("------------------\n");
    bytes = NULL;
    skipFlag = mcsTRUE;
    miscDynBufSetCommentPattern(&dynBuf, "\0");
    printf("skipFlag = '%s Comment Skiping' | commentPattern = '%s'\n",
           (skipFlag == mcsFALSE?"WITHOUT":"WITH"),
           miscDynBufGetCommentPattern(&dynBuf));
    while ((bytes = miscDynBufGetNextLinePointer(&dynBuf, bytes, skipFlag))
           != NULL)
    {
        printf("------------------\n");
        printf("miscDynBufGetNextLinePointer() = '%s'\n", bytes);
    }
    printf("------------------\n");
    bytes = NULL;
    skipFlag = mcsTRUE;
    miscDynBufSetCommentPattern(&dynBuf, "#");
    printf("skipFlag = '%s Comment Skiping' | commentPattern = '%s'\n",
           (skipFlag == mcsFALSE?"WITHOUT":"WITH"),
           miscDynBufGetCommentPattern(&dynBuf));
    while ((bytes = miscDynBufGetNextLinePointer(&dynBuf, bytes, skipFlag))
           != NULL)
    {
        printf("------------------\n");
        printf("miscDynBufGetNextLinePointer() = '%s'\n", bytes);
    }
    printf("\n");

    miscDynBufDestroy(&dynBuf);
    printf("---------------------------------------------------------------\n");
    printf("                      THAT'S ALL FOLKS ;)                      \n");
    printf("---------------------------------------------------------------\n");

    exit(0);
}


void displayExecStatus(mcsCOMPL_STAT executionStatusCode)
{
    if (executionStatusCode == FAILURE)
    {
        printf("%s\n", FAILED);
        errCloseStack();
    }
    else
    {
        printf("%s\n", SUCCEED);
    }

    return;
}

void displayDynBuf(miscDYN_BUF *dynBuf)
{
    mcsUINT32 bytesNumber = 0;
    char*     tmp         = NULL;

    printf("miscDynBufGetNbStoredBytes = ");
    if (miscDynBufGetNbStoredBytes(dynBuf, &bytesNumber) == FAILURE)
    {
        printf("FAILURE.\n");
    }
    else
    {
        printf("'%d'.\n", bytesNumber);
    }

    printf("miscDynBufGetNbAllocatedBytes = ");
    if (miscDynBufGetNbAllocatedBytes(dynBuf, &bytesNumber) == FAILURE)
    {
        printf("FAILURE.\n");
    }
    else
    {
        printf("'%d'.\n", bytesNumber);
    }

    printf("miscDynBufGetCommentPattern = ");
    tmp = (char*)miscDynBufGetCommentPattern(dynBuf);
    if (tmp == NULL)
    {
        printf("FAILURE.\n");
    }
    else
    {
        printf("\"%s\".\n", tmp);
    }

    printf("miscDynBufGetBufferPointer = ");
    tmp = miscDynBufGetBufferPointer(dynBuf);
    if (tmp == NULL)
    {
        printf("FAILURE.\n");
    }
    else
    {
        printf("\"%s\".\n", tmp);
    }

    errResetStack();
}

/*___oOo___*/
