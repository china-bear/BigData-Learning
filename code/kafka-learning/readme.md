
## 错误
Exception in thread "main" java.lang.RuntimeException: com.fasterxml.jackson.databind.exc.InvalidFormatException: Cannot deserialize value of type `java.util.Date` from String "05-NOV-2018": expected format "dd-MM-yyyy"

# 把 maven 插件生成pojo类文件 手动更新
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", timezone = "IST")
  改成：
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MMM-yyyy", locale = "en", timezone = "IST")