git checkout -b training-branch

git add newfile
git reset newfile - “rollback from staging to working)”

# ???

git remove -v
git branch -a
git reset <commit> - “rollback from repository to working”

git commit -m “hello, first commit from my branch”
git push origin training-branch

create a pull request, review, merge, delete branch

git remote add upstream https://github.com/Data4Democracy/github-playground.git

git stash
git stash list
git stash pop

git log -n 3
git log --oneline
git log --stat
git log --author="FangYang970206"
git log --grep="Merge pull request" --oneline
git log --oneline hello.py
git log --since 2020/06/12

# GUI tools, ie: SourceTree

# Revert & Reset & Clean & Amend

git revert <commit-id>
git reset --hard 4688de48cd34cebff708d47cafeaf6d7568e6549 - “rollback from
repository” ; git push --force (reset remote repository)
git clean --force
git commit --amend (never amend the commit in remote repository)

123
