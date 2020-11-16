# 新版Flink Java环境开发快速搭建

## Maven创建FLINK项目(三种方式选一种即可)
1. 基于 Maven Archetype 构建，直接使用下面的 mvn 语句来进行构建
直接使用下面的 mvn 语句来进行构建，然后根据交互信息的提示，依次输入 groupId , artifactId 以及包名等信息后等待初始化的完成
mvn archetype:generate \
  -DarchetypeGroupId=org.apache.flink \
  -DarchetypeArtifactId=flink-quickstart-java \
  -DarchetypeVersion=1.10.2 \
  -DgroupId=org.myorg.quickstart \
  -DartifactId=quickstart	\
  -Dversion=0.1 \
  -Dpackage=org.myorg.quickstart \
  -DinteractiveMode=false

2. 使用官方脚本快速构建
为了更方便的初始化项目，官方提供了快速构建脚本，可以直接通过以下命令来进行调用：

curl https://flink.apache.org/q/quickstart.sh | bash -s 1.10.2

该方式其实也是通过执行 maven archetype 命令来进行初始化，相比于第一种方式，该种方式只是直接指定好了 groupId ，artifactId ，version 等信息而已。

把生成的项目文件导入IDEA即可

3. 使用 IDEA 构建
直接在项目创建页面选择 Maven Flink Archetype 进行项目初始化：


如果你的 IDEA 没有上述 Archetype， 可以通过点击右上角的 ADD ARCHETYPE ，来进行添加，依次填入所需信息，这些信息都可以从上述的 archetype:generate 语句中获取。
点击  OK 保存后，该 Archetype 就会一直存在于你的 IDEA 中，之后每次创建项目时，只需要直接选择该 Archetype 即可：

选中 Flink Archetype ，然后点击 NEXT 按钮，之后的所有步骤都和正常的 Maven 工程相同。


## IDEA运行环境设置(二种方式选一种即可)

1. 使用 IDEA 创建项目pom.xml  添加profile 配置后在 id 为 add-dependencies-for-IDEA 的 profile 中，
所有的核心依赖都被标识为 compile，此时你可以无需改动任何代码，只需要在 IDEA 的 Maven 面板中勾选该 profile后再Reload ALL MAVEN PROJECTS，
即可直接在 IDEA 中运行 Flink 项目,或者会导致IDEA中启动项目时会抛出 ClassNotFoundException 异常。
   
        <!-- This profile helps to make things run out of the box in IntelliJ -->
        <!-- Its adds Flink's core classes to the runtime class path. -->
        <!-- Otherwise they are missing in IntelliJ, because the dependency is 'provided' -->
        <profile>
            <id>add-dependencies-for-IDEA</id>

            <activation>
                <property>
                    <name>idea.version</name>
                </property>
            </activation>

            <dependencies>
                <dependency>
                    <groupId>org.apache.flink</groupId>
                    <artifactId>flink-scala_${scala.binary.version}</artifactId>
                    <scope>compile</scope>
                </dependency>
                <dependency>
                    <groupId>org.apache.flink</groupId>
                    <artifactId>flink-streaming-scala_${scala.binary.version}</artifactId>
                    <scope>compile</scope>
                </dependency>
                <dependency>
                    <groupId>org.scala-lang</groupId>
                    <artifactId>scala-library</artifactId>
                    <scope>compile</scope>
                </dependency>
            </dependencies>
        </profile>
    </profiles>


2. 在IntelliJ IDEA中做相关设置 Include dependencies with “Provided” scope
RUN-->RUN/DEBUG  --> "Run/Debug Configurations"


##
https://www.cnblogs.com/toudoushaofeichang/p/11606255.html
https://juejin.im/post/6844903999703891976
https://segmentfault.com/a/1190000022205667


