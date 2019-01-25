task :default => 'lint'

task :safety => [] do
    sh 'safety check'
end

task :editorconfig => [] do
    sh 'git ls-files -z | grep -av patch | xargs -0 -r -n 100 $(npm bin)/eclint check'
end

task :lint => [
    :safety,
    :editorconfig
] do
end
