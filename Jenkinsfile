pipeline {
    agent any

    tools {
        maven 'Maven3' // Nome da instalação do Maven no Jenkins
        jdk 'Java21'   // Nome da instalação do JDK no Jenkins
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/SEU_USUARIO/crud-springboot.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                // Deploy para cloud
                //sh 'docker build -t crud-springboot:latest .' // Se usar Docker
                //sh 'docker run -d -p 8080:8080 crud-springboot:latest'
            }
        }
    }
}
