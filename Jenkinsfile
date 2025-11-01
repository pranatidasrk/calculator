pipeline {
    agent any

    environment {
        DOCKERHUB_REPO = 'yourdockerhubuser/calculator'  // change to your repo
        IMAGE_TAG = "${env.BUILD_NUMBER ?: 'local'}"
        EC2_HOST = 'your-ec2-public-ip'  // replace this
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                bat 'mvn -B -DskipTests clean package'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    bat "docker build -t %DOCKERHUB_REPO%:%IMAGE_TAG% ."
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASS')]) {
                    bat '''
                        echo %DOCKERHUB_PASS% | docker login -u %DOCKERHUB_USER% --password-stdin
                        docker push %DOCKERHUB_REPO%:%IMAGE_TAG%
                    '''
                }
            }
        }

        stage('Deploy to EC2') {
            steps {
                sshagent(['ec2-ssh']) {
                    bat """
                        ssh -o StrictHostKeyChecking=no ec2-user@%EC2_HOST% ^
                        "docker pull %DOCKERHUB_REPO%:%IMAGE_TAG% && ^
                        docker stop calculator || true && ^
                        docker rm calculator || true && ^
                        docker run -d --name calculator -p 8080:8080 %DOCKERHUB_REPO%:%IMAGE_TAG%"
                    """
                }
            }
        }
    }

    post {
        always {
            echo '✅ Pipeline finished successfully (or with errors logged).'
        }
    }
}
