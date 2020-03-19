<#list test as map>
    <#if map_index = 0>
        <request>
    <#else>
        <detail>
    </#if>
<#list map?keys as itemkey>
    <${itemkey}>${map[itemkey]!("")}</${itemkey}>
</#list>
    <#if map_index = 0>
        </request>
    <#else>
        </detail>
    </#if>
</#list>
