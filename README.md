# ParkAid - Parkinson's freezing of gait assist

## Problem Statement
Parkinson's disease (PD) is a progressive and degenerative neurological movement disorder that is currently affecting approximately 6.3 million people worldwide. The number of PD patients is expected to increase to over 10 million by 2030. Based on existing studies, PD is the second most common age-related neurodegenerative disease after Alzheimer’s disease. There is no cure, but medications exist that temporarily help control some of the symptoms. One of the most common symptoms experienced by patients in their later stages of PD is what is known as the freezing of gait (FOG). This symptom is often observed as unresponsive to medication. FOG is characterized by temporary and involuntary inability to move. Consequently, this sudden freezing leads to dangerous situations that cause patients to lose balance and fall, which can be fatal. Approximately 38% of patients with PD fall each year resulting in serious injuries or even death. An efficient way to overcome this symptom is to predict the freezing of gait before it even happens and immediately provide audiovisual feedback to help patients recover gait control.  

## Project Objectives
The objective of this project is to develop a wearable device that predicts FOG events in advance and provides prompt audio-visual feedback helping patients adjust posture to avoid imbalance that otherwise may lead to falls that can be fatal. Inertial measurement unit (IMU) (accelerometers/gyroscopes) sensor situated on the patient’s lower leg must collect real time data of the patient’s gait. A prediction algorithm must compare this data with normal gait data to predict/detect FOG events so that a prompt audio-visual feedback can be provided. Overall, the device must be compact, inexpensive, have high precision in prediction/detection and provide immediate feedback that enables patients to recover gait control and avoid fall incidents.

## Mobile App
The mobile application is a companion software application that connects to the wearable device and provides controls and functionality related to the wearable device. The mobile application connects to the device wirelessly(via bluetooth) and it provides the user, the ability to select between different types of feedback. The user can also check logs of freeze of gait and fall incidents through the mobile application. In case, the device is unable to predict the fall and provide feedback in time to prevent the fall, the mobile application automatically contacts the people on the emergency contacts list predefined by the user. The mobile application is also user friendly and the user can easily learn the functionality.
