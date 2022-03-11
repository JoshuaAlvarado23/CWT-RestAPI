pipeline {
    agent any
    stages {
        stage('getCode') {
            steps {
                git 'https://github.com/JoshuaAlvarado23/CWT-RestAPI.git'
                
                withSonarQubeEnv(installationName = 'sonarqube1') {
                bat 'mvn clean sonar:sonar'
            }
            post {
                success {
                    echo "Success code fetch from GitHub"
                }
                failure {
                    echo "Failed getting code from repository"
                }
            }
        }
        
        stage('testCode') {
            steps {
                bat 'mvn test'
            }
            post {
                success {
                    echo "All Test Succeed"
                }
                failure {
                    echo "Tests Failed"
                }
            }
        }
        stage('build') {
            steps {
            	bat 'if not exist "C:\\Users\\collabera\\Desktop\\CWT-RestAPI" mkdir "C:\\Users\\collabera\\Desktop\\CWT-RestAPI"'
                bat 'mvn clean'
              	bat 'mvn deploy -DaltDeploymentRepository=finalSnapshot::default::file:C:\\Users\\collabera\\Desktop\\CWT-RestAPI -DskipTests=true'
            }
            
            post {
                success {
                    echo "Deployed!"
                }
                failure {
                    echo "Deployment Failed."
                }
            }
        }
        
    }
}
