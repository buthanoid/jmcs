#ifndef miscDate_H
#define miscDate_H
/*******************************************************************************
 * JMMC project
 *
 * "@(#) $Id: miscDate.h,v 1.8 2005-05-26 09:53:31 lafrasse Exp $"
 *
 * History
 * -------
 * $Log: not supported by cvs2svn $
 * Revision 1.7  2005/03/03 15:45:28  gluck
 * Code review corrections: invert argument in prototypes to be compliant with I, I/O, O parameter order
 *
 * Revision 1.6  2005/02/22 10:23:35  gluck
 * Code review corrections: doxygen file header
 *
 * Revision 1.5  2005/02/22 10:08:15  gluck
 * Code review corrections
 *
 * Revision 1.4  2005/02/15 09:37:52  gzins
 * Added CVS log as file modification history
 *
 * lafrasse  02-Aug-2004  Forked from misc.h to isolate miscDate headers
 *                        Moved mcs.h include in from miscDate.c
 *
 ******************************************************************************/

/**
 * @file
 * Declaration of miscDate functions.
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

mcsCOMPL_STAT miscGetUtcTimeStr  (const mcsUINT32    precision,
                                        mcsSTRING32  utcTime);

mcsCOMPL_STAT miscGetLocalTimeStr(const mcsUINT32    precision,
                                        mcsSTRING32  localTime);


#ifdef __cplusplus
}
#endif

#endif /*!miscDate_H*/

/*___oOo___*/
