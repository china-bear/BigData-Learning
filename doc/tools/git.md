#https://mp.weixin.qq.com/s?__biz=Mzg2NTAzMTExNg==&mid=2247483866&idx=1&sn=fe987cd24448bd6eb2138cfd43a82cf8&scene=19#wechat_redirect
#https://cloud.tencent.com/developer/article/1355182
#https://www.ruanyifeng.com/blog/2015/12/git-cheat-sheet.html

http://rogerdudler.github.io/git-guide/index.zh.html
http://marklodato.github.io/visual-git-guide/index-zh-cn.html

#查看git配置
git config --list

#配置git用户信息
git config --global user.name  [username]  #名称
git config --global user.email [usermail]  #邮箱
#设置提交和检出均不转换换行符的， 如不设置可能会导致从git clone下来的文件，换行符改为CRLF，导致部署到Azkaban后运行报错
git config --global core.autocrlf false

#在当前目录新建一个本地Git代码库
git init
# 新建一个目录，将其初始化为Git代码库
$ git init <project-name>

#克隆一个远程仓库到本地仓库
git clone [url] <本地目录>

#查看文件状态
git status <filename>

# 增加工作区文件到暂存区，文件红色变为绿色
git add [file1] [file2] ...
git add [dir]
git add .

#提交暂存区的文件到本地仓库
git commit -m 'message'

# 显示暂存区和工作区的差异
git diff
# 显示暂存区和上一个commit的差异
$ git diff --cached [file]
# 显示工作区与当前分支最新commit之间的差异
$ git diff HEAD

#恢复暂存区的指定文件到工作区,注意不要忘记"--",不写就成了检出分支了
git checkout -- [filename]
git checkout .

这里有两种情况：
一种是filename自修改后还没有被放到暂存区，现在，撤销修改就回到和版本库一模一样的状态；
一种是filename已经添加到暂存区后，又作了修改，现在，撤销修改就回到添加到暂存区后的状态。
总之，就是让这个文件回到最近一次git commit或git add时的状态

#从版本库中删除文件
#同时从工作区和索引中删除文件,即本地的文件也被删除了。
git rm  <filename>
git commit -m "remove filename"

git rm -r  [目录]
#从索引中删除文件。但是本地文件还存在， 只是不希望这个文件被版本控制
git rm --cached



#显示版本提交历史的详细信息
git log
git log --pretty=oneline
git log --all --decorate --oneline --graph
#显示版本提交历史的简要信息 还会显示分支切换操作历史和git pull的操作历史
git reflog

#根据commit_id回退到指定版本,但工作区不变
git reset commId
#回退到最近的一个版本
git reset HEAD --hard 
##不保存所有变更,同时重置暂存区和工作区，与指定commit id一致
git reset commId --hard
##保留变更且变更内容处于Staged
git reset commId --soft
##保留变更且变更内容处于Modified
git reset commId --mixed

#储藏当前分支所有内容
git stash
#list查看当前分支储藏列表
git stash list
#恢复指定储藏内容
git stash apply
#删除指定储藏内容
git stash drop
#恢复并删除指定储藏内容
git stash pop

#将本地提交内容推送到远程仓库
git push
#设置上流分支，其实就是与远程仓库分支关联了，后面直接push
git push --set-upstream origin dev

#git分支中常用指令
## 列出所有本地分支
git branch
git branch -vv
##  列出所有远程分支
git branch -r
##  新建一个分支，但依然停留在当前分支
git branch [branch-name]
#选择or切换到[branch-name]分支
git checkout [branch-name]
## 新建一个分支，并切换到该分支
git checkout -b [branch] <template>   
git checkout -b [branch] <origin> <template>
#拉取远程分支并创建本地分支
git checkout -b 本地分支名 origin/远程分支名
## 合并指定分支的变更到当前分支
$ git merge [branch]
## 删除分支
$ git branch -d [branch-name]
## 删除远程分支
$ git push origin --delete [branch-name]
$ git branch -dr [remote/branch]

##
#下载远程仓库的所有变动, 根据需要合并指定分区
git fetch <remote>
#只想取回特定分支的更新,需要手动切换到该本地分支
git fetch <远程主机名> <分支名>
git fetch origin master #下载远程 origin 主机的master 分支
#拉取远程分支并创建本地分支,需要手动切换到该本地分支
git fetch origin 远程分支名x:本地分支名x


#下载远程分支最新内容并自动与本地分支合并 git fetch + git merge
get pull <remote> <branch>
git pull <远程主机名> <远程分支名>:<本地分支名>

#重新排列commit
git rebase [branch-name]
git add .
git rebase --continue

## git标签管理
#版本打一个新标签 tag，tag就是一个让人容易记住的有意义的名字，它跟某个commit绑在一起,如果没有带commit id，默认标签是打在最新提交的commit上的
git tag v0.9 <commit id>
#查看标签
git tag
git show v0.9
#推送一个本地标签到远程仓库
git push origin <tagname>
#推送全部未推送过的本地标签到远程仓库
git push origin --tags
#删除一个本地标签
git tag -d <tagname>
#删除一个远程标签
git push origin :refs/tags/<tagname>

#远程代码强行覆盖本地代码
git fetch --all
git reset --hard origin/master
git pull

#git 文件的四种状态
Untracked: 未跟踪, 此文件在文件夹中, 但并没有加入到git库, 不参与版本控制. 通过git add 状态变为Staged.
Unmodify: 文件已经入库, 未修改, 即版本库中的文件快照内容与文件夹中完全一致. 这种类型的文件有两种去处, 如果它被修改, 而变为Modified. 如果使用git rm移出版本库, 则成为Untracked文件
Modified: 文件已修改, 仅仅是修改, 并没有进行其他的操作. 这个文件也有两个去处, 通过git add可进入暂存staged状态, 使用git checkout 则丢弃修改过, 返回到unmodify状态, 这个git checkout即从库中取出文件, 覆盖当前修改 !
Staged: 暂存状态. 执行git commit则将修改同步到库中, 这时库中的文件和本地文件又变为一致, 文件为Unmodify状态. 执行git reset HEAD filename取消暂存, 文件状态为Modified

#Git本地有三个工作区域：
工作目录（Working Directory）
暂存区(Stage/Index)
资源库(Repository或Git Directory)
如果在加上远程的git仓库(Remote Directory)就可以分为四个工作区域

