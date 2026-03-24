$envFile = ".env"

Get-Content $envFile | ForEach-Object {
    if ($_ -match "^\s*$" -or $_ -match "^\s*#") { return }
    $parts = $_ -split "="
    $name = $parts[0]
    $value = $parts[1]
    set-item -path env:$name -value $value
}

sam local start-api --port 8001
