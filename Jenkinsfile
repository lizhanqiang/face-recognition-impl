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
          echo 'package face-recognition-service'
          dir('face-recognition-service'){
            sh 'mvn clean package -Dmaven.test.skip=true'
          }
      }
    }
  }
}
