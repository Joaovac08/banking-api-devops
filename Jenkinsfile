pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "joaovac06/banking-api"
        DOCKER_TAG   = "v${env.BUILD_NUMBER}-${env.GIT_COMMIT?.take(7) ?: 'local'}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Fazendo checkout do código..."
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                echo "Compilando e rodando testes..."
                sh 'mvn clean verify'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    jacoco execPattern: 'target/jacoco.exec'
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo "Construindo imagem Docker: ${DOCKER_IMAGE}:${DOCKER_TAG}"
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} -t ${DOCKER_IMAGE}:latest ."
            }
        }

        stage('Security Scan (Trivy)') {
            steps {
                echo "Escaneando vulnerabilidades..."
                sh 'trivy image --exit-code 0 --severity HIGH,CRITICAL --no-progress ${DOCKER_IMAGE}:${DOCKER_TAG} || true'
            }
        }

        stage('Push to Registry') {
            when {
                branch 'main'
            }
            steps {
                echo "Publicando imagem no Docker Hub..."
                withCredentials([usernamePassword(
                    credentialsId: 'dockerhub-credentials',
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS'
                )]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                        docker push ${DOCKER_IMAGE}:latest
                    '''
                }
            }
        }

        stage('Notify') {
            steps {
                echo "Pipeline concluído com sucesso!"
                echo "Imagem: ${DOCKER_IMAGE}:${DOCKER_TAG}"
            }
        }
    }

    post {
        success {
            echo "Pipeline finalizado com sucesso!"
        }
        failure {
            echo "Pipeline falhou. Verifique os logs dos estágios."
        }
        always {
            cleanWs()
        }
    }
}
