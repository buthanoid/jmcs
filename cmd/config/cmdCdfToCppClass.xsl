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
            
            <xsl:document href="{concat('../include/',concat($className,'.h'))}" method="text">
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
 * MCS Headers
 */
#include "cmd.h"

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
<xsl:value-of select="$className"/>(string name, string params);
    virtual ~<xsl:value-of select="$className"/>();<xsl:for-each select="./params/param">
<xsl:variable name="paramName"> <xsl:call-template name="convertcase"><xsl:with-param name="toconvert" select="./name"/> <xsl:with-param name="conversion">upfirst</xsl:with-param></xsl:call-template> </xsl:variable>
    virtual mcsCOMPL_STAT get<xsl:value-of select="$paramName"/>(<xsl:call-template name="GetMcsTypeForParam"><xsl:with-param name="paramNode" select="."/></xsl:call-template>*<xsl:value-of select="./name"/>);<xsl:if test="./@optional">
        virtual mcsLOGICAL isDefined<xsl:value-of select="$paramName"/>();</xsl:if><xsl:if test="boolean(./defaultValue)">
    virtual mcsLOGICAL hasDefault<xsl:value-of select="paramName"/>();
    virtual mcsCOMPL_STAT getDefault<xsl:value-of select="$paramName"/>(<xsl:call-template name="GetMcsTypeForParam"><xsl:with-param name="paramNode" select="."/></xsl:call-template>*<xsl:value-of select="./name"/>);</xsl:if>

</xsl:for-each>

protected:

private:
    // Declaration of copy constructor and assignment operator as private
    // methods, in order to hide them from the users.
     <xsl:value-of select="$className"/>(const <xsl:value-of select="$className"/>&amp;);
     <xsl:value-of select="$className"/>&amp; operator=(const <xsl:value-of select="$className"/>&amp;);

};

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
#include "cmd.h"
#include "<xsl:value-of select="$className"/>.h"
#include "<xsl:value-of select="$moduleName"/>Private.h"

/*
 * Class constructor
 */

/**
 * Constructs a new class for an easier access for parameters of the
 * <xsl:value-of select="$className"/> COMMAND.
 */
 <xsl:value-of select="$className"/>::<xsl:value-of select="$className"/>(string name, string params):cmdCMD(name, params)
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
     <xsl:variable name="paramName"> <xsl:call-template name="convertcase"><xsl:with-param name="toconvert" select="./name"/> <xsl:with-param name="conversion">upfirst</xsl:with-param></xsl:call-template> </xsl:variable>
/**
 * Get the value of the parameter <xsl:value-of select="./name"/>.
 *
 * \param <xsl:value-of select="./name"/> a pointer where to store the parameter.
 * 
 * \return an MCS completion status code (SUCCESS or FAILURE).
 */ 
mcsCOMPL_STAT <xsl:value-of select="$className"/>::get<xsl:value-of select="$paramName"/>(<xsl:call-template name="GetMcsTypeForParam"><xsl:with-param name="paramNode" select="."/></xsl:call-template>*<xsl:value-of select="./name"/>)
{
    logExtDbg("<xsl:value-of select="$className"/>::get<xsl:value-of select="$paramName"/>()");	
    return getParamValue("<xsl:value-of select="./name"/>",<xsl:value-of select="./name"/>);
}
<xsl:if test="./@optional">
/**
 * Check if the optional parameter <xsl:value-of select="./name"/> is defined. 
 * 
 * \return mcsTRUE or mcsFALSE if it is not defined.
 */ 
 mcsLOGICAL <xsl:value-of select="$className"/>::isDefined<xsl:value-of select="$paramName"/>()
{
    logExtDbg("<xsl:value-of select="$className"/>::isDefined<xsl:value-of select="$paramName"/>()");
    return isDefined("<xsl:value-of select="./name"/>");
}
</xsl:if><xsl:if test="boolean(./defaultValue)">
/**
* Check if the parameter <xsl:value-of select="./name"/> has a default value.
 *
 * \return mcsTRUE or mcsFALSE if it has no default value.
 */ 
 mcsLOGICAL <xsl:value-of select="$className"/>::hasDefault<xsl:value-of select="$paramName"/>()
{
    logExtDbg("<xsl:value-of select="$className"/>::hasDefault<xsl:value-of select="$paramName"/>()");
    return hasDefaultValue("<xsl:value-of select="./name"/>");
}

/**
 * Get the default value of the parameter <xsl:value-of select="./name"/>.
 *
 * \param <xsl:value-of select="./name"/> a pointer where to store the parameter.
 * 
 * \return an MCS completion status code (SUCCESS or FAILURE).
 */ 
mcsCOMPL_STAT <xsl:value-of select="$className"/>::getDefault<xsl:value-of select="$paramName"/>(<xsl:call-template name="GetMcsTypeForParam"><xsl:with-param name="paramNode" select="."/></xsl:call-template>*<xsl:value-of select="./name"/>)
{
    logExtDbg("<xsl:value-of select="$className"/>::getDefault<xsl:value-of select="$paramName"/>()");
    return getDefaultParamValue("<xsl:value-of select="./name"/>",<xsl:value-of select="./name"/>);
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
    

  <xsl:variable name="lcletters">abcdefghijklmnopqrstuvwxyz</xsl:variable>

  <xsl:variable name="ucletters">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:variable>

  <xsl:template name='convertcase'>
    <xsl:param name='toconvert' />

    <xsl:param name='conversion' />

    <xsl:choose>
      <xsl:when test='$conversion="lower"'>
        <xsl:value-of
        select="translate($toconvert,$ucletters,$lcletters)" />
      </xsl:when>

      <xsl:when test='$conversion="upper"'>
        <xsl:value-of
        select="translate($toconvert,$lcletters,$ucletters)" />
      </xsl:when>

      <xsl:when test='$conversion="proper"'>
        <xsl:call-template name='convertpropercase'>
          <xsl:with-param name='toconvert'>
            <xsl:value-of
            select="translate($toconvert,$ucletters,$lcletters)" />
          </xsl:with-param>
        </xsl:call-template>
      </xsl:when>
      
      <xsl:when test='$conversion="upfirst"'>
        <xsl:call-template name='convertpropercase'>
          <xsl:with-param name='toconvert'>
            <xsl:value-of
                select="$toconvert" />
          </xsl:with-param>
        </xsl:call-template>
      </xsl:when>

      <xsl:otherwise>
        <xsl:value-of select='$toconvert' />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name='convertpropercase'>
    <xsl:param name='toconvert' />

    <xsl:if test="string-length($toconvert) > 0">
      <xsl:variable name='f'
      select='substring($toconvert, 1, 1)' />

      <xsl:variable name='s' select='substring($toconvert, 2)' />

      <xsl:call-template name='convertcase'>
        <xsl:with-param name='toconvert' select='$f' />

        <xsl:with-param name='conversion'>upper</xsl:with-param>
      </xsl:call-template>

      <xsl:choose>
        <xsl:when test="contains($s,' ')">
        <xsl:value-of select='substring-before($s," ")' />

          
        <xsl:call-template name='convertpropercase'>
          <xsl:with-param name='toconvert'
          select='substring-after($s," ")' />
        </xsl:call-template>
        </xsl:when>

        <xsl:otherwise>
          <xsl:value-of select='$s' />
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
