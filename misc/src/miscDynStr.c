/*******************************************************************************
* JMMC project
* 
* "@(#) $Id: miscDynStr.c,v 1.4 2004-08-02 14:25:25 lafrasse Exp $"
*
* who       when         what
* --------  -----------  -------------------------------------------------------
* lafrasse  13-Jul-2004  Created
* lafrasse  22-Jul-2004  Removed all '\0' from char arrays
* lafrasse  23-Jul-2004  Added error code factorization, plus
*                        miscDynStrGetStringFromTo parameter refinments
* lafrasse  02-Aug-2004  Moved mcs.h include to miscDynStr.h
*
*
*******************************************************************************/

/**
 * \file
 * Contains all the miscDynStr Dynamic String functions definitions.
 * 
 * All the algorithms behind Dynamic String management are either in this file,
 * or in miscDynBuf.c.
 *
 * \sa To see all Dynamic String related functions declarations and a minimal
 * code example, see miscDynStr.h
 * \sa To see all Dynamic Buffer related functions declarations and code
 * example, see miscDynBuf.h
 * \sa To see all the other 'misc' module functions declarations, see misc.h
 */

static char *rcsId="@(#) $Id: miscDynStr.c,v 1.4 2004-08-02 14:25:25 lafrasse Exp $"; 
static void *use_rcsId = ((void)&use_rcsId,(void *) &rcsId);

/* 
 * System Headers
 */
#include <string.h>
#include <stdlib.h>


/*
 * MCS Headers 
 */
#include "err.h"


/* 
 * Local Headers
 */
#include "miscDynStr.h"
#include "miscPrivate.h"
#include "miscErrors.h"


/* 
 * Local functions declaration 
 */
static        mcsUINT32 miscDynStrVerifyStringParameterValidity(
                                             char              *str);


/* 
 * Local functions definition
 */

/**
 * Verify if a received string (a null terminated char array) is valid or not.
 *
 * \param str the address of the receiving, already allocated extern buffer
 *
 * \return the string length, or 0 if it is not valid
 */
static        mcsUINT32 miscDynStrVerifyStringParameterValidity(
                                             char              *str)
{
    /* Test the 'str' parameter validity */
    if (str == NULL)
    {
        errAdd(miscERR_NULL_PARAM, "str");
        return 0;
    }

    /* Return the number of bytes stored in the received string including its
     * ending '\0'
     */
    return(strlen(str) + 1);
}


/*
 * Public functions definition
 */

/**
 * Give back a part of a Dynamic Buffer as a null terminated string, in an
 * already allocated extern buffer.
 *
 * \warning The first Dynamic Buffer byte has the position value defined by the
 * miscDYN_BUF_BEGINNING_POSITION macro.\n\n
 *
 * \param dynBuf the address of a Dynamic Buffer structure
 * \param str the address of the receiving, already allocated extern buffer
 * \param from the first Dynamic Buffer byte to be copied in the extern string
 * \param to the last Dynamic Buffer byte to be copied in the extern string
 *
 * \return an MCS completion status code (SUCCESS or FAILURE)
 */
mcsCOMPL_STAT miscDynStrGetStringFromTo     (miscDYN_BUF       *dynBuf,
                                             char              *str,
                                             const mcsUINT32   from,
                                             const mcsUINT32   to)
{
    /* Try to get the requested bytes array from the Dynamic Buffer */
    if (miscDynBufGetBytesFromTo(dynBuf, str, from, to) == FAILURE)
    {
        return FAILURE;
    }

    /* Compute the 'str' buffer length */
    mcsUINT32 stringLength = (to - miscDYN_BUF_BEGINNING_POSITION) 
                             - (from - miscDYN_BUF_BEGINNING_POSITION);

    /* Add an '\0' at the end of the received result */
    str[stringLength + 1] = '\0';

    return SUCCESS;
}


/**
 * Replace a given range of Dynamic Buffer bytes by an extern string without its
 * ending '\\0'.
 *
 * The Dynamic Buffer replaced bytes will bve overwritten. Their range can be
 * smaller or bigger than the extern string length. If the end of the Dynamic
 * Buffer is to be replaced, the string ending '\\0' is keeped.
 *
 * \warning The first Dynamic Buffer byte has the position value defined by the
 * miscDYN_BUF_BEGINNING_POSITION macro.\n\n
 *
 * \param dynBuf the address of a Dynamic Buffer structure
 * \param str the address of the extern string to be written in
 * \param from the position of the first Dynamic Buffer byte to be substituted
 * \param to the position of the last Dynamic Buffer byte to be substituted
 *
 * \return an MCS completion status code (SUCCESS or FAILURE)
 */
mcsCOMPL_STAT miscDynStrReplaceStringFromTo (miscDYN_BUF       *dynBuf,
                                             char              *str,
                                             const mcsUINT32   from,
                                             const mcsUINT32   to)
{
    /* Test the 'str' parameter validity */
    mcsUINT32 stringLength = 0;
    if ((stringLength = miscDynStrVerifyStringParameterValidity(str)) == 0)
    {
        return FAILURE;
    }

    /* If the end of the Dynamic Buffer is to be replaced... */
    if (to == dynBuf->storedBytes)
    {
        /* Increment the number of bytes to be copied from the string in order
         * to add the ending '\0' to the Dynamic Buffer
         */
        ++stringLength;
    }
    
    /* Try to replace Dynamic Buffer specified bytes with the string (with or
     * without its ending '\0')
     */
    return(miscDynBufReplaceBytesFromTo(dynBuf, str, stringLength-1, from, to));
}


/**
 * Copy an extern string (a null terminated char array) at the end of a
 * Dynamic Buffer, adding an '\\0' at the end of it.
 *
 * \param dynBuf the address of a Dynamic Buffer structure
 * \param str the address of the extern string to be written in
 *
 * \return an MCS completion status code (SUCCESS or FAILURE)
 */
mcsCOMPL_STAT miscDynStrAppendString        (miscDYN_BUF       *dynBuf,
                                             char              *str)
{
    /* Test the 'str' parameter validity */
    mcsUINT32 stringLength = 0;
    if ((stringLength = miscDynStrVerifyStringParameterValidity(str)) == 0)
    {
        return FAILURE;
    }

    /* Try to get the Dynamic Buffer stored bytes number */
    mcsUINT32 storedBytes = 0;
    if (miscDynBufGetStoredBytesNumber(dynBuf, &storedBytes) == FAILURE)
    {
        return FAILURE;
    }

    /* If the Dynamic Buffer already contain something... */
    if (storedBytes != 0)
    {

        /* Try to get the last character of the Dynamic Buffer */
        char lastDynBufChr = '\0';
        if (miscDynBufGetByteAt(dynBuf, &lastDynBufChr, storedBytes) == FAILURE)
        {
            return FAILURE;
        }
    
        /* If the Dynamic Buffer was already holding a null-terminated string...
         */
        if (lastDynBufChr == '\0')
        {
            /* Try to remove the ending '\0' from the Dynamic Buffer */
            if (miscDynBufDeleteBytesFromTo(dynBuf, storedBytes, storedBytes)
                == FAILURE)
            {
                return FAILURE;
            }
        }
    }

    /* Try to append the string bytes, including its '\0' */
    return(miscDynBufAppendBytes(dynBuf, str, stringLength));
}


/**
 * Insert an extern string (a null terminated char array) without its ending
 * '\\0' in a Dynamic Buffer at a given position.
 *
 * The Dynamic Buffer bytes are not overwritten, but shiffted to the right.
 *
 * \warning The first Dynamic Buffer byte has the position value defined by the
 * miscDYN_BUF_BEGINNING_POSITION macro.\n\n
 *
 * \param dynBuf the address of a Dynamic Buffer structure
 * \param str a pointer to the extern string to be inserted
 * \param position the position of the first Dynamic Buffer byte to write at
 *
 * \return an MCS completion status code (SUCCESS or FAILURE)
 */
mcsCOMPL_STAT miscDynStrInsertStringAt      (miscDYN_BUF       *dynBuf,
                                             char              *str,
                                             const mcsUINT32   position)
{
    /* Test the 'str' parameter validity */
    int stringLength = 0;
    if ((stringLength = miscDynStrVerifyStringParameterValidity(str)) == 0)
    {
        return FAILURE;
    }

    /* Try to insert the string without its ending '\0' in the Dynamic Buffer */
    return(miscDynBufInsertBytesAt(dynBuf, str, stringLength - 1, position));
}


/*___oOo___*/
