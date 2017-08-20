task :default => :lint

task :shfmt => [] do
  sh 'stank . | xargs shfmt -w -i 4'
end

task :bashate => [] do
  sh 'stank . | xargs bashate'
end

task :shlint => [] do
  sh 'stank . | xargs shlint'
end

task :checkbashisms => [] do
  sh 'stank . | xargs checkbashisms -n -p'
end

task :shellcheck => [] do
  sh 'stank . | xargs shellcheck'
end

task :lint => [
  :shfmt,
  :bashate,
  :shlint,
  :checkbashisms,
  :shellcheck
] do
end
