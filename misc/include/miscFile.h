#ifndef miscFile_H
#define miscFile_H
/*******************************************************************************
 * JMMC project
 *
 * "@(#) $Id: miscFile.h,v 1.11 2005-04-06 09:31:50 gluck Exp $"
 *
 * History
 * -------
 * $Log: not supported by cvs2svn $
 * Revision 1.10  2005/02/15 09:37:52  gzins
 * Added CVS log as file modification history
 *
 * lafrasse  02-Aug-2004  Forked from misc.h to isolate miscFile headers
 *                        Moved mcs.h include in from miscFile.c
 * lafrasse  23-Aug-2004  Changed miscGetEnvVarValue API
 * lafrasse  25-Sep-2004  Added miscFileExists
 * lafrasse  27-Sep-2004  Added miscLocateFileInPath
 * lafrasse  30-Sep-2004  Added miscLocateFile
 * lafrasse  01-Oct-2004  Changed miscResolvePath API for consistency
 * lafrasse  07-Oct-2004  Changed miscFileExists API
 *
 ******************************************************************************/

/**
 * \file
 * Declaration of miscFile functions.
 */

/* The following piece of code alternates the linkage type to C for all 
functions declared within the braces, which is necessary to use the 
functions in C++-code.
*/
#ifdef __cplusplus
extern "C" {
#endif


/* 
 * MCS Headers
 */
#include "mcs.h"


/*
 * Pubic functions declaration
 */
 
char *        miscGetFileName       (const char       *fullPath);

char *        miscGetExtension      (char             *fullPath);

mcsCOMPL_STAT miscYankExtension     (char             *fullPath,
                                     char             *extension);

char*         miscResolvePath       (const char       *orginalPath);

mcsCOMPL_STAT miscGetEnvVarValue    (const char       *envVarName,
                                     char             *envVarValueBuffer,
                                     mcsUINT32         envVarValueBufferLength);

mcsCOMPL_STAT miscYankLastPath      (char             *path);

mcsLOGICAL    miscFileExists        (const char       *fullPath,
                                     mcsLOGICAL        addError);

char*         miscLocateFileInPath  (const char       *path,
                                     const char       *fileName);

char*         miscLocateFile        (const char       *fileName);

#ifdef __cplusplus
}
#endif

#endif /*!miscFile_H*/

/*___oOo___*/
