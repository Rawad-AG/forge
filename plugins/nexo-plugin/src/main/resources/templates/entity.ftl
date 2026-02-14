package ${model.pkg};

<#list model.imports as import>
import ${import};
</#list>

<#list model.annotations as ann>
${ann.toString()}
</#list>
public class ${model.name} {

    <#list model.fields as field>
    <#list field.annotations as ann>
    ${ann.toString()}
    </#list>
    ${field.toString()};

    </#list>


    <#list model.methods as method>
    <#list method.annotations as ann>
    ${ann.toString()}
    </#list>
    ${method.toString()} 

    </#list>
}
