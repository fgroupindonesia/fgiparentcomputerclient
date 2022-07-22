git add .
git commit -m "Removing the installer from the git package"
git push origin main
git gc --prune
git pull
pause