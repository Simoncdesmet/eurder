pipeline {
    agent any

    tools {
        jdk 'jdk-12.0.2'
    }

    stages {
            stage('build') {
                steps {
                    bat 'mvn clean test-compile'
                }
            }
        stage('test') {
            steps {
                bat 'mvn test'
              junit allowEmptyResults: true, keepLongStdio: true, testResults: 'api/target/surefire-reports/	TEST-com.simon.eurder.api.CustomerControllerIntegrationTest.xml	'
            }
        }
    }

}
