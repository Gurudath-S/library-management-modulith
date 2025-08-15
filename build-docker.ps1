# Build script for Library Modulith Docker image (Windows)

param(
    [string]$ImageName = "library-modulith",
    [string]$ImageTag = "latest",
    [string]$DockerfilePath = "."
)

Write-Host "Building Docker image: $ImageName`:$ImageTag" -ForegroundColor Green

try {
    # Build the Docker image
    Write-Host "Running docker build..." -ForegroundColor Yellow
    docker build -t "$ImageName`:$ImageTag" $DockerfilePath
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "Docker image built successfully!" -ForegroundColor Green
        
        # Optional: Tag with version from pom.xml
        try {
            $Version = (mvn help:evaluate -Dexpression=project.version -q -DforceStdout 2>$null) -replace "`n|`r"
            if ([string]::IsNullOrWhiteSpace($Version)) {
                $Version = "1.0.0"
            }
            docker tag "$ImageName`:$ImageTag" "$ImageName`:$Version"
            Write-Host "Tagged image as $ImageName`:$Version" -ForegroundColor Green
        } catch {
            Write-Host "Could not tag with version from pom.xml, using default" -ForegroundColor Yellow
        }
        
        # Show image info
        Write-Host "`nImage details:" -ForegroundColor Cyan
        docker images | Select-String $ImageName
        
        Write-Host "`nBuild completed successfully!" -ForegroundColor Green
        Write-Host ""
        Write-Host "To run the container:" -ForegroundColor Yellow
        Write-Host "  docker run -p 8080:8080 $ImageName`:$ImageTag" -ForegroundColor White
        Write-Host ""
        Write-Host "To run with docker-compose:" -ForegroundColor Yellow
        Write-Host "  docker-compose up -d" -ForegroundColor White
        
    } else {
        Write-Host "Docker build failed!" -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "Error during build: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}
