pipeline{

agent any

stages {
   stage('Preparation') { 
      steps{
      	git branch: 'Backend', url: 'https://github.com/Revature-1704-Java/tapestry-project-2.git'
      }
   }
   stage('Build') {
       steps{       
       //move dist folder into our resources folder for our maven project
       /*sh '''cd front
            ng build -p
            mv dist ../Tapestry/src/main/resources/'''*/
            
      // Run the maven build
        
		slackSend color: '#888888', message: 'Building Backend Project'
        sh '''cd Tapestry
        mvn clean package'''
       }
	   
	   post{
		failure{
			slackSend color: '#FFFFFF', message: 'Backend Building Failed'
		}
	   }
    
   }
   stage('Results') {
      steps{
      junit '**/target/surefire-reports/*.xml'
      archive 'target/*.jar'
      }
   }
   
   stage('Deploy'){
   	steps{
	   slackSend color: '#0F0F0F', message: 'Backend Build was successful'
       //sh 'java -jar 0.0.1-SNAPSHOT.jar'
       slackSend color: '#000000', message: 'Backend Build was Deployed'
	}
   }
}
}
