task :default => :lint

task :shlint => [] do
  sh 'find . \( -wholename \'*/node_modules*\' -o -name \'*.bat\' \) -prune -o -type f \( -wholename \'*/bin/*\' -o -name \'*.sh\' -o -name \'*.bashrc*\' -o -name \'.*profile*\' -o -name \'*.envrc*\' \) -print | xargs shlint'
end

task :checkbashisms => [] do
  sh 'find . \( -wholename \'*/node_modules*\' -o -name \'*.bat\' \) -prune -o -type f \( -wholename \'*/bin/*\' -o -name \'*.sh\' -o -name \'*.bashrc*\' -o -name \'.*profile*\' -o -name \'*.envrc*\' \) -print | xargs checkbashisms -n -p'
end

task :shellcheck => [] do
  sh 'find . \( -wholename \'*/node_modules*\' -o -name \'*.bat\' \) -prune -o -type f \( -wholename \'*/bin/*\' -o -name \'*.sh\' -o -name \'*.bashrc*\' -o -name \'.*profile*\' -o -name \'*.envrc*\' \) -print | xargs shellcheck'
end

task :lint => [
  :shlint,
  :checkbashisms,
  :shellcheck
] do
end
