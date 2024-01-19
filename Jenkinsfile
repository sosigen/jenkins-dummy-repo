pipeline {
    // agent pozwala doprecyzować którego węzła chce użyć
    // any oznacza jakiegokolwiek
    agent any
    triggers {
        // sprawdzaj githuba co minute
        pollSCM('* * * * *')
    }
    parameters {
        booleanParam(name: 'debug', defaultValue: false, description: 'Enable debug mode')
        string(name: 'git_branch', defaultValue: 'main', description: 'Git branch to build')
    }
    // ta sekcja nie jest konieczna, jeśli nie zależy nam na konkretnej wersji
    tools {
        maven 'maven'
    }

    // stages to odpowiednik 'akcji budowania' z UI
    stages {
        stage('Checkout') {
            steps {
                // zaleta używania Jenkinsfile, w klauzuli skrypt możemy zrobić
                // co się nam żywnie podoba
                script {
                    echo "Checking out branch ${params.git_branch}"
                    // pobierz kod z gałęzi podanej w parametrze
                    checkout([$class: 'GitSCM', branches: [[name: "*/${params.git_branch}"]], userRemoteConfigs: [[url: 'https://github.com/sosigen/jenkins-dummy-repo.git']]])
                    if (params.debug) {
                        echo "Checked out the specified branch."
                    }
                }
            }
        }
        stage('Build') {
            steps {
                script {
                    if (params.debug) {
                        echo "Starting build process..."
                    }
                    def mvnHome = tool 'maven'
                    // jako że Jenkins działa na windowsie do wykonywania komend używam 'bat'
                    // na Linuxie użyłbym sh
                    bat "${mvnHome}\\bin\\mvn -B clean package"
                    if (params.debug) {
                        echo "Build completed."
                    }
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    if (params.debug) {
                        echo "Running tests..."
                    }
                    def mvnHome = tool 'maven'
                    bat "${mvnHome}\\bin\\mvn test"
                    if (params.debug) {
                        echo "Testing completed."
                    }
                }
            }
            // post to odpowiednik akcji po budowaniu
            post {
                always {
                    script {
                        // wywołanie wtyczki tworzacej wykres
                        junit '**/target/surefire-reports/TEST-*.xml'
                        if (params.debug) {
                            echo "Published test results."
                        }
                    }
                }
            }
        }
        // jak może wyglądać dystrybucja w Jenkinsie?
        // prosty przykład wywołania innego joba
        stage('Deploy') {
            steps {
                script {
                    if (params.debug) {
                        echo "Starting deployment..."
                    }
                    // komendą build mogę wywołać inny job
                    build job: 'utils/dummy-deploy', wait: false, node: 'test-node'
                    if (params.debug) {
                        echo "Deployment completed."
                    }
                }
            }
        }
    }
    post {
        // always wykona się niezależnie od statusu joba, nawet przy błędzie
        always {
            // wyczyść przestrzeń roboczą
            cleanWs()
        }
    }
}
