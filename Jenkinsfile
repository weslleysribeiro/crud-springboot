pipeline {
    agent any

    tools {
        maven 'maven' // Nome da instalação do Maven no Jenkins
        jdk 'jdk-21'   // Nome da instalação do JDK no Jenkinst
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/weslleysribeiro/crud-springboot.git'
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
