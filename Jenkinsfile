pipeline {
    agent any
    triggers {
        pollSCM('H/1 * * * *')
    }
    parameters {
        booleanParam(name: 'debug', defaultValue: false, description: 'Enable debug mode')
        string(name: 'git_branch', defaultValue: 'main', description: 'Git branch to build')
    }
    tools {
        maven 'maven'
    }
    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out branch ${params.git_branch}"
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
                    def mvnHome = tool 'Maven 3.9.6'
                    sh "${mvnHome}/bin/mvn test"
                    if (params.debug) {
                        echo "Testing completed."
                    }
                }
            }
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
            cleanWs()
            if (params.debug) {
                echo "Cleaned up the workspace."
            }
        }
    }
}
