pipeline {
agent any


environment {
DOCKERHUB_REPO = 'yourdockerhubuser/calculator' // replace
IMAGE_TAG = "${env.BUILD_NUMBER ?: 'local'}"
}


stages {
stage('Checkout') {
steps { checkout scm }
}


stage('Build (Maven)') {
steps {
bat 'mvn -B -DskipTests package'
}
}


stage('Build Docker Image') {
steps {
script {
bat "docker build -t ${DOCKERHUB_REPO}:${IMAGE_TAG} ."
}
}
}


stage('Push to Docker Hub') {
steps {
withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKERHUB_USER', passwordVariable: 'DOCKERHUB_PASS')]) {
bat 'echo $DOCKERHUB_PASS | docker login -u $DOCKERHUB_USER --password-stdin'
bat "docker push ${DOCKERHUB_REPO}:${IMAGE_TAG}"
}
}
}


stage('Deploy to EC2') {
steps {
// Assumes you have an SSH key credential in Jenkins with id 'ec2-ssh'
// and the EC2 instance has Docker installed and accessible via `ec2-user` (adjust user if different)
sshagent(['ec2-ssh']) {
sh "ssh -o StrictHostKeyChecking=no ec2-user@${EC2_HOST} 'docker pull ${DOCKERHUB_REPO}:${IMAGE_TAG} && docker stop calculator || true && docker rm calculator || true && docker run -d --name calculator -p 8080:8080 ${DOCKERHUB_REPO}:${IMAGE_TAG}'"
}
}
}
}


post {
always {
echo 'Pipeline finished.'
}
}
}
