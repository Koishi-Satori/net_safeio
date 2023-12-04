## 这是个squirrel SafeIO的简易替代品

- 基于kotlin+netty的http服务器
- 高稳定性低占用
- 在JVM上运行, 平台无关
- 目前功能: 合并/覆写/查询

---

Example code: [example.nut](./example.nut)

Options:

- -p, -port: bind new port
  - e.g. -p 1145
- -dir -d: set the data storage dir
  - e.g. -d "/usr/xxxx"
- -file -f: set the store file name, file name check regex: [a-zA-Z_.]+
  - e.g. -f "ifp_data.json"

----
建议将API部署到本地 然后使用localhost:[port]作为api_url

传输的数据必须为由table<string, string>序列化来的json, 同时返回的数据也为json格式

返回的response直接使用response.body结合DecodeJSON反序列化Json即可拿到储存的table
```squirrel
// GetDataImpl is used for getting response.
HttpRequestResponse response = GetDataImpl()
table data = DecodeJSON(response.body)
// Your process code
// ..
```

运行要求: JDK 17以上

## JDK LINKS

- azul prime(best jdk for linux only): https://www.azul.com/products/prime/
- azul zulu(all platform): https://www.azul.com/downloads/#zulu

## 启动及参数
启动命令行:  java -classpath ./net_safeio.jar top.kkoishi.safeio.MainKt
请将参数加在命令行的后面并且用**空格隔开**