#查看git配置
git config --list

#配置git用户信息
git config --global user.name  [username]  #名称
git config --global user.email [usermail]  #邮箱

#在当前目录新建一个本地Git代码库
git init 

#克隆一个远程仓库到本地仓库
git clone [url]

#查看文件状态
git status <filename>
git status

#
git diff


# 增加工作区文件到暂存区，文件红色变为绿色
git add <filename>
git add .

#工作区的修改全部撤销,注意不要忘记"--",不写就成了检出分支了!
git checkout -- [filename]
git checkout .

这里有两种情况：
一种是filename自修改后还没有被放到暂存区，现在，撤销修改就回到和版本库一模一样的状态；
一种是filename已经添加到暂存区后，又作了修改，现在，撤销修改就回到添加到暂存区后的状态。
总之，就是让这个文件回到最近一次git commit或git add时的状态

#
git rm  <filename>

#
git checkout commId

#提交暂存区的文件到本地仓库
git commit -m 'message'


#显示版本提交历史的详细信息
git log
git log --pretty=oneline
#显示版本提交历史的简要信息 还会显示分支切换操作历史和git pull的操作历史
git reflog

#把暂存区的修改撤销掉重新放回工作区
git reset commId
##不保存所有变更
git reset commId --hard
##保留变更且变更内容处于Staged
git reset commId --soft
##保留变更且变更内容处于Modified
git reset commId --mixed

git stash
git stash list
git stash pop




#推送本地到远程仓库
git push


#git分支中常用指令

## 列出所有本地分支
git branch
##  列出所有远程分支
git branch -r
##  新建一个分支，但依然停留在当前分支
git branch [branch-name]
## 新建一个分支，并切换到该分支
git checkout -b [branch] <template>
git checkout -b [branch] <origin> <template>
## 合并指定分支到当前分支
$ git merge [branch]
## 删除分支
$ git branch -d [branch-name]
## 删除远程分支
$ git push origin --delete [branch-name]
$ git branch -dr [remote/branch]
##
git fetch


# fetch + 本地merge
get pull

#
git rebase



#git 文件的四种状态
Untracked: 未跟踪, 此文件在文件夹中, 但并没有加入到git库, 不参与版本控制. 通过git add 状态变为Staged.
Unmodify: 文件已经入库, 未修改, 即版本库中的文件快照内容与文件夹中完全一致. 这种类型的文件有两种去处, 如果它被修改, 而变为Modified. 如果使用git rm移出版本库, 则成为Untracked文件
Modified: 文件已修改, 仅仅是修改, 并没有进行其他的操作. 这个文件也有两个去处, 通过git add可进入暂存staged状态, 使用git checkout 则丢弃修改过, 返回到unmodify状态, 这个git checkout即从库中取出文件, 覆盖当前修改 !
Staged: 暂存状态. 执行git commit则将修改同步到库中, 这时库中的文件和本地文件又变为一致, 文件为Unmodify状态. 执行git reset HEAD filename取消暂存, 文件状态为Modified

#Git本地有三个工作区域：
工作目录（Working Directory）
暂存区(Stage/Index)
资源库(Repository或Git Directory)
如果在加上远程的git仓库(Remote Directory)就可以分为四个工作区域。
