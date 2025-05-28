pipeline {
    agent any
    stages {
        stage("Hello") {
            steps {
                echo "Hello World"
            }
        }
        stage("git pull") {
            steps {
                checkout([$class: "GitSCM", branches: [[name: "${branch}"]], extensions: [], userRemoteConfigs: [[credentialsId: "e2804a17-90fa-4c8c-8f30-5985b4e5bb4c", url: "http://192.168.0.91:8088/matrixsphere/matrix-sphere-boot-dependencies.git"]]])            }
        }
        stage("maven build") {
            steps {
                sh '''
                    mvn -U clean package -Dmaven.test.skip=true
                    docker images
                   '''
            }
        }
    }
    post {
        cleanup {
            deleteDir()
            dir("${workspace}@tmp") {
                deleteDir()
            }
            dir("${workspace}@script") {
                deleteDir()
            }
        }
    }
}
