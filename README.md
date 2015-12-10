#EmotiSearch!


####About:
Emotisearch began as a submission for GameStop & IBM's Hackathon at San Jose State University (November 2015). It was designed to be a unique way for customers to use thir own image as a way to search for new games, explore different genres, or simply entertain themselves for a few minutes.

Emotisearch takes the user's picture and a few questions (ie. "What consoles do you own?") and generates a list of potentially interesting games based off of three physical attributes: Age, Gender, and Emotion. The picture is taken inside the application and sent to Microsoft's Project Oxford API. The returned attibutes, internally mapped to different boolean search terms, are then used to query the GameStop Product API for a results list.

####Implementation:

This application is written in SpringJS, Bootstrap, HTML5, & CSS3. Emotisearch  querys against GameStop's product and customer APIs to perform filtered game searches and to retrieve the customer's personal preferences. Additionally, Microsoft's Project Oxford Computer Vision APIs are used to perform the emotional and physical characterstics analysis on the user generated images.
