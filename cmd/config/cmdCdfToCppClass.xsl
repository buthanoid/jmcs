<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="text" omit-xml-declaration="yes" indent="no"/>

    <xsl:param name="moduleName"/>
    <!--
    <xsl:include href="include.xsl"/>
    -->
    <xsl:variable name="autoGeneratedC"><xsl:text>/*
 * 
 * This file has been automatically generated
 * 
 * !!!!!!!!!!!  DO NOT MANUALLY EDIT THIS FILE  !!!!!!!!!!!
 */</xsl:text>
</xsl:variable>

    <!-- ********************************************************* -->
    <!-- This TEMPLATE is the main part                            -->
    <!-- it calls two main templates for each cmd nodes            -->
    <!-- INPUT : the module name                                   -->
    <!-- OUTPUT: the generated files  usind <mod><MNEMO>_CMD.C     -->
    <!-- ********************************************************* -->
    <xsl:template match="/">
 
        <xsl:for-each select="/cmd">
            <xsl:variable name="className" select="concat(concat($moduleName,./mnemonic),'_CMD')"/>
            
        <xsl:document href="{concat($className,'.H')}" method="text">
            <xsl:value-of select="$autoGeneratedC"/>
            <xsl:call-template name="cmdCppHeader">
                <xsl:with-param name="className" select="$className"/>
            </xsl:call-template>
        </xsl:document> 
        <xsl:document href="{concat($className,'.C')}" method="text">
            <xsl:value-of select="$autoGeneratedC"/>
            <xsl:call-template name="cmdCppClass">
                <xsl:with-param name="className" select="$className"/>
            </xsl:call-template>
        </xsl:document> 

        </xsl:for-each>
    </xsl:template>
        
    <!-- ********************************************************* -->
    <!-- This TEMPLATE is the main part to generate the c++ header -->
    <!-- file from a given command node.                           -->
    <!-- ********************************************************* -->
    <xsl:template name="cmdCppHeader">
#ifndef <xsl:value-of select="$className"/>_H
#define <xsl:value-of select="$className"/>_H

/**
 * \file
 * <xsl:value-of select="$className"/> class declaration.
 * This file has been automatically generated. If this file is missing in your
 * modArea, just type make all to regenerate.
 */

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

/*
 * Class declaration
 */
        
    <xsl:text>&#xA;/**
 * This class is intented to be used for a
 * reception of the </xsl:text> <xsl:value-of select="mnemonic"/> <xsl:text> command 
 */&#xA;</xsl:text>
class <xsl:value-of select="$className"/>: public cmdCMD
{
public:
    <xsl:value-of select="$className"/>();
    ~<xsl:value-of select="$className"/>();<xsl:for-each select="./params/param">
    mcsCOMPL_STAT get_<xsl:value-of select="./name"/>(<xsl:call-template name="GetMcsTypeForParam"><xsl:with-param name="paramNode" select="."/></xsl:call-template> *<xsl:value-of select="./name"/>);<xsl:if test="./@optional">
    mcsLOGICAL isDefined_<xsl:value-of select="./name"/>();</xsl:if><xsl:if test="boolean(./defaultValue)">
    mcsLOGICAL hasDefault_<xsl:value-of select="./name"/>();
    mcsCOMPL_STAT getDefault_<xsl:value-of select="./name"/>(<xsl:call-template name="GetMcsTypeForParam"><xsl:with-param name="paramNode" select="."/></xsl:call-template> *<xsl:value-of select="./name"/>);</xsl:if>

</xsl:for-each>

protected:

private:
    // Declaration of copy constructor and assignment operator as private
    // methods, in order to hide them from the users.
     <xsl:value-of select="$className"/>(const <xsl:value-of select="$className"/>&amp;);
     <xsl:value-of select="$className"/>&amp; operator=(const <xsl:value-of select="$className"/>&amp;);

}

#endif /*!<xsl:value-of select="$className"/>_H*/

/*___oOo___*/
</xsl:template>
    
    
    <!-- ********************************************************* -->
    <!-- This TEMPLATE is the main part to generate the c++ class  -->
    <!-- file from a given command node.                           -->
    <!-- ********************************************************* -->
    <xsl:template name="cmdCppClass">
/**
 * \file
 * <xsl:value-of select="$className"/> class definition.
 */
 
 
/*
 * System Headers
 */
#include &lt;stdio.h&gt;
#include &lt;iostream&gt;
using namespace std;

/*
 * MCS Headers
 */
#include "log.h"

/*
 * Local Headers
 */
#include "cmdTRUC.h"


/*
 * Class constructor
 */

/**
 * Constructs a new class for an easier access for parameters of the
 * <xsl:value-of select="$className"/> COMMAND.
 */
<xsl:value-of select="$className"/>::<xsl:value-of select="$className"/>()
{

}

/*
 * Class destructor
 */

/**
 * Class destructor
 */
<xsl:value-of select="$className"/>::~<xsl:value-of select="$className"/>()
{

}

/*
 * Public methods
 */
        
<xsl:for-each select="./params/param">
/**
 * Get the value of the parameter <xsl:value-of select="./name"/>.
 *
 * \param <xsl:value-of select="./name"/> a pointer where to store the parameter.
 * 
 * \return an MCS completion status code (SUCCESS or FAILURE).
 */ 
mcsCOMPL_STAT <xsl:value-of select="$className"/>::get_<xsl:value-of select="./name"/>(<xsl:call-template name="GetMcsTypeForParam"><xsl:with-param name="paramNode" select="."/></xsl:call-template> *<xsl:value-of select="./name"/>)
{
    mcsLogExtDbg("<xsl:value-of select="$className"/>::get_<xsl:value-of select="./name"/>()");	
    return getParam("<xsl:value-of select="./name"/>",<xsl:value-of select="./name"/>);
}
<xsl:if test="./@optional">
/**
 * Check if the optional parameter <xsl:value-of select="./name"/> is defined. 
 * 
 * \return mcsTRUE or mcsFALSE if it is not defined.
 */ 
mcsLOGICAL <xsl:value-of select="$className"/>::isDefined_<xsl:value-of select="./name"/>()
{
    mcsLogExtDbg("<xsl:value-of select="$className"/>::isDefined_<xsl:value-of select="./name"/>()");
    return isDefined("<xsl:value-of select="./name"/>");
}
</xsl:if><xsl:if test="boolean(./defaultValue)">
/**
* Check if the parameter <xsl:value-of select="./name"/> has a default value.
 *
 * \return mcsTRUE or mcsFALSE if it has no default value.
 */ 
mcsLOGICAL <xsl:value-of select="$className"/>::hasDefault_<xsl:value-of select="./name"/>()
{
    mcsLogExtDbg("<xsl:value-of select="$className"/>::hasDefault_<xsl:value-of select="./name"/>()");
    return hasDefault("<xsl:value-of select="./name"/>");
}

/**
 * Get the default value of the parameter <xsl:value-of select="./name"/>.
 *
 * \param <xsl:value-of select="./name"/> a pointer where to store the parameter.
 * 
 * \return an MCS completion status code (SUCCESS or FAILURE).
 */ 
mcsCOMPL_STAT <xsl:value-of select="$className"/>::getDefault_<xsl:value-of select="./name"/>(<xsl:call-template name="GetMcsTypeForParam"><xsl:with-param name="paramNode" select="."/></xsl:call-template> *<xsl:value-of select="./name"/>)
{
    mcsLogExtDbg("<xsl:value-of select="$className"/>::getDefault_<xsl:value-of select="./name"/>()");
    return getDefaultParam("<xsl:value-of select="./name"/>",<xsl:value-of select="./name"/>);
}
</xsl:if>

</xsl:for-each>

/*___oOo___*/
</xsl:template>


    <!-- *********************************************************** -->
    <!-- This TEMPLATE outputs a declaration block for the parameter -->
    <!-- *********************************************************** -->
    <xsl:template name="GetCDeclForParam">
        <xsl:param name="paramNode"/>
        <xsl:call-template name="PrintParamValue"><xsl:with-param name="paramNode" select="$paramNode"/></xsl:call-template>
        <xsl:value-of select="$paramNode/name"/>
	</xsl:template>
    
    <!-- *********************************************************** -->
    <!-- This TEMPLATE outputs a declaration block for the parameter -->
    <!-- *********************************************************** -->
    <xsl:template name="GetMcsTypeForParam">
        <xsl:param name="paramNode"/>
        <xsl:choose>
            <xsl:when test="contains($paramNode/type,'integer')">
                <xsl:text>mcsINT32 </xsl:text>
            </xsl:when>
            <xsl:when test="contains($paramNode/type,'string')">
                <xsl:text>char *</xsl:text>
            </xsl:when>
            <xsl:when test="contains($paramNode/type,'boolean')">
                <xsl:text>mcsLOGICAL </xsl:text>
            </xsl:when>
            <xsl:when test="contains($paramNode/type,'double')">
                <xsl:text>mcsDOUBLE </xsl:text>
            </xsl:when>
        </xsl:choose>
	</xsl:template>
  
    <!-- *********************************************************** -->
    <!-- This TEMPLATE outputs the printf's formatting code for      -->
    <!-- the given  param                                            -->
    <!-- *********************************************************** -->
    <xsl:template name="PrintParamValue">
        <xsl:param name="paramNode"/>
        <xsl:choose>
            <xsl:when test="contains($paramNode/type,'integer')">
                <xsl:text>d</xsl:text>
            </xsl:when>
            <xsl:when test="contains($paramNode/type,'string')">
                <xsl:text>s</xsl:text>
            </xsl:when>
            <xsl:when test="contains($paramNode/type,'boolean')">
                <xsl:text>d</xsl:text>
            </xsl:when>
            <xsl:when test="contains($paramNode/type,'double')">
                <xsl:text>f</xsl:text>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>


