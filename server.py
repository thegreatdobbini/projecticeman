import json
import os
import numpy as np
import cv2
import matplotlib.pyplot as plt
import io
import time

from google.cloud import vision
from google.cloud.vision import types

PATH = "/var/ftp/upload/"

# Grab all file names and add them to a list
# Copy all images to the local dir.
def grab_files():
	f_names = []

	for l in os.listdir(PATH):
		if os.path.isfile(PATH+l) and l[-3:] == "jpg":
			f_names.append(l)
			os.rename(PATH+l,os.path.dirname(os.path.realpath(__file__))+"/pics/"+l)

	return f_names


# Takes filename, returns team id or empty strings
def obj_rec(filename):
	print("--------------BEFORE")
	client = vision.ImageAnnotatorClient()
	print("here")
	with open("./pics/"+filename,"rb") as image_file:
		content = image_file.read()
	out = ""

	try:
		image = types.Image(content=content)
		response = client.logo_detection(image=image)
		logos = response.logo_annotations
		print('Logos:')
		for logo in logos:
			print(logo)
			print(type(logo.description))
			if ("kansas" in logo.description) or ("chiefs" in logo.description) or ("arrowhead" in logo.description):
				out = "KC"
			elif ("new england" in logo.description) or ("patriots" in logo.description):
				out = "NE"
			print(logo.description)

	except:
		print("Fail")
	return out


# Master. Calls the rest and puts it in PATH
def place_info():
	NONE = '''<?xml version = "1.0"?>
<class>
	<team found="false">
	</team>
</class>'''

	f_names = grab_files()

	if f_names: # Not empty
		for file in f_names:
			payload = NONE

			team_id = obj_rec(file) # Find team_id, can be empty
			if team_id:
				payload = get_info(team_id) # API calls, returns JSON looking string

			with open(PATH+file[:-3]+"xml","w+") as f:
				f.write(payload)

			os.remove(os.path.dirname(os.path.realpath(__file__))+"/pics/"+file)


# Take ID of team, return XML string object
def get_info(team_id):
	NAMES = {"KC":"Kansas City Chiefs","NE":"New England Patriots"}


	payload = '''<?xml version = "1.0"?>
<class>
	<team found="true">
		<name>{}</name>
	</team>
</class>'''.format(NAMES[team_id])

	return payload

	#URL = "http://api.sportradar.us/"
	#KEY = "?api_key=eqfstqzfwgrffgazqxbjw48j"

	#for y in ["2018","2017","2016"]:
	#	STANDINGS = "nfl-t1/teams/REG/{}/standings{}".format("y","xml")
	#	try:
	#		r.requests.get(URL+STANDINGS+KEY)
	#		j_obj = json.loads(r.text)
	#		j_
	#	except:
	#		payload += ''




if __name__ == "__main__":
	while 1:
		place_info()
		time.sleep(.2)