pipeline {
  agent any

  stages {
    stage('checkout') {
      steps {
        echo 'checkout source code'
        git 'https://github.com/lizhanqiang/face-recognition-impl.git'
      }
    }
    stage('build_face_recognition_service') {
      steps {
        echo 'killing face-recognition-service......'
        sh "ps aux | grep face-recognition-service | grep -v grep | awk '{print $2}' | xargs kill -9"
        dir('face-recognition-service') {
          sh 'mvn clean package -Dmaven.test.skip=true'
        }
        dir('face-recognition-service/target') {
          sh 'nohup java -jar face-recognition-service-1.0-SNAPSHOT.jar >service.log  2>&1 &'
        }
      }
    }
  }
}
