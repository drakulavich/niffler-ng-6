<html>
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpResponseAttachment" -->
<head>
    <meta http-equiv="content-type" content="text/html; charset = UTF-8">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/2.2.3/jquery.min.js" crossorigin="anonymous"></script>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.3.6/js/bootstrap.min.js" crossorigin="anonymous"></script>

    <link type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.0/styles/github.min.css" rel="stylesheet"/>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/highlight.js/8.0/highlight.min.js"></script>
    <script type="text/javascript">hljs.initHighlightingOnLoad();</script>

    <style>
        pre {
            white-space: pre-wrap;
        }
    </style>
</head>
<body>
<div>Status code <#if data.responseCode??>${data.responseCode} <#else>Unknown</#if></div>
<#if data.url??><div>${data.url}</div></#if>

<#if data.body??>
    <h4>Body</h4>
    <div>
        <pre><code>${data.body}</code></pre>
    </div>
</#if>

<#if (data.headers)?has_content>
    <h4>Headers</h4>
<pre><code>
<#list data.headers as name, value>
    ${name}: ${value!"null"}
</#list>
</code></pre>
</#if>


<#if (data.cookies)?has_content>
    <h4>Cookies</h4>
<pre><code>
<#list data.cookies as name, value>
    ${name}: ${value!"null"}
</#list>
</code></pre>
</#if>
</body>
</html>
