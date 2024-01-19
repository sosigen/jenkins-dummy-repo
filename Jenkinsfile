pipeline {
    // na jakim węźle wykonać ten job?
    agent any

    // triggery dla wykonania joba
    triggers {
        // format: MINUTE HOUR DayOfMonth MONTH DayOfWeek
        pollSCM('H/1 * * * *') // Sprawdza repozytorium co minutę
        // w prawdziwym scenariuszu zdecydowanie optymalniejsze byłoby użycie webhooka
        // pollSCM może wtedy służyc jako backup, gdy webhook nie zadziala, np raz dziennie 
    }
    
    parameters {
        booleanParam(name: 'debug', defaultValue: false, description: 'Enable debug mode')
        string(name: 'git_branch', defaultValue: 'main', description: 'Git branch to build')
    }

    // jakich narzędzi użyć?
    tools {
        maven 'Maven 3.9.6'
        jdk 'OpenJDK 21'
    }


    // poszczególne etapy pipeline'u. Rozwiązanie czysto logiczne, 
    // wszystko mogłoby się dziać w pojedyńczym etapie
    stages {
        stage('Checkout') {
            steps {
                echo "Checking out branch ${params.git_branch}"
                // wbudowana funkcja Jenkinsa, klonuje repozytorium
                checkout([$class: 'GitSCM', branches: [[name: "*/${params.git_branch}"]], userRemoteConfigs: [[url: 'https://github.com/sosigen/jenkins-dummy-repo.git']]])
                if (params.debug) {
                    echo "Checked out the specified branch."
                }
            }
        }

        stage('Build') {
            steps {
                // klauzula 'script' - wymagana dla wielu typowo programistycznych działań,
                // np. tworzenia zmiennych, pętli, funkcji
                script {
                    if (params.debug) {
                        echo "Starting build process..."
                    }
                    def mvnHome = tool 'Maven 3.9.6'
                    // sh czyli najzwyklejszy bash
                    sh "${mvnHome}/bin/mvn -B clean package"
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
                    def mvnHome = tool 'Maven 3.6.3'
                    sh "${mvnHome}/bin/mvn test"
                    if (params.debug) {
                        echo "Testing completed."
                    }
                }
            }
            // klauzula 'post' - wykona się po danym etapie
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    if (params.debug) {
                        echo "Published test results."
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    if (params.debug) {
                        echo "Starting deployment..."
                    }
                    // zlecam wykonanie deployu innemu jobowi
                    build job: 'utils/dummy-deploy', wait: false, node: 'test-node'
                    if (params.debug) {
                        echo "Deployment completed."
                    }
                }
            }
        }
    }

    post {
        always {
            // czyszcze przestrzeń roboczą - sklonowane repozytorium, pliki pomocnicze
            cleanWs()
            if (params.debug) {
                echo "Cleaned up the workspace."
            }
        }
    }
}
