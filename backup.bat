@echo off
mysqldump -u root horus > horus_backup.sql
echo Backup realizado com sucesso!
pause
exit