#=============================================================#
# Description
# Python script to test Critical section execution 
# 
# Environment
# ------------
# Python script   test_CrticalSection.py 
#
# Command
# ------------
# python <Python script> < Test script>
# Eg:
# python test_CrticalSection.py
# 
# 
#  
# History
# ------------
# 07/13/15     Pavan Kangokar     Initial Release
#
#   
#=============================================================#

#==========#
# IMPORTS  #
#==========#
import os, sys, time, datetime, visa, globals

#===========#
# CONSTANTS #
#===========#
CURR=""
PREV=""
PREVVECTOR=[]
CURRVECTOR=[]
#============#
# Classes    #
#============#


class Controller:	
	
	def __init__(self,fileName):
	#initialization of Controller Class
			self.fileName = fileName
			self.ERRORFLAG=False	
			self.outdatafile = str("log_CrticalSection"+".txt")#singleton Pattern to create only one instance of this object(file)
			os.remove(self.outdatafile) if os.path.exists(self.outdatafile) else None
			self.fileobjPwr=open(self.outdatafile , 'a')# output file
			self.fileobjPwr.write("Test")
	def readFile(self):
	#This method Read the file and creats corresponding objects to read the Agilent values based on the input
		#print 'readFile'
		#read the input file
		self.fileobjPwr.write("#######################################\Testing:"+'System Time:'+time.ctime()+'\n#######################################\n')
		file=open(self.fileName,'r')
		line =file.readline()
		#print line,'\n'
		PREV=""
		CURR=""
		PREVVECTOR=[]
		CURRVECTOR=[]
		for line in iter(file):
			# print line,'\n' 
			
			line=line.rstrip('\n') #chomp new line
			if not line.strip():
				continue
				
			value=line.split('\t')# split the string wrt tab,
			node = value[0]
			if(node.startswith('Node')):
				#self.fileobjPwr.write(node+':\n')
				
				if(value[1]=='CS-Enter'):
					PREV=CURR
					CURR="CS_ENTER"
				elif(value[1]=='CS-Exit'):
					PREV=CURR
					CURR="CS_EXIT"
					
				elif(value[1]=='Vector'):
					PREVVECTOR=CURRVECTOR
					StringValue=value[2]
					CURRVECTOR = [int(k) for k in StringValue.split(',')]
					print 	"CURRVECTOR=",CURRVECTOR
					print 	"PREVVECTOR=",PREVVECTOR
					
				if(CURR==PREV):
					print 	"PREV=",PREV
					print 	"CURR=",CURR
					CURR=""
					print   "Node=",node
					self.ERRORFLAG=True
					self.fileobjPwr.write("MULTIPLE CS execution at "+node+"\n\n")
				if(sum(CURRVECTOR)<sum(PREVVECTOR)):
					CURRVECTOR=[]
					PREVVECTOR=[]
					self.fileobjPwr.write("Vector Failed  at "+node+"\n\n")
					self.ERRORFLAG=True	
			
		if (self.ERRORFLAG==False):
			self.fileobjPwr.write("Succesffuly Critcal execution without overlap of critical section and there is happened before relationships between consecutative vector clocks\n\n")
#===============#
# MAIN PROGRAM  #
#===============#
sys.stdout.write("\n\n\n")
sys.stdout.write("================================================================================\n")
sys.stdout.write("Crtical Section Check Test Started ...........\n")
sys.stdout.write("Test started, please wait until finished or press Ctrl+C to terminate anytime...\n")
sys.stdout.write("================================================================================\n")

indatafile = str("Output.txt")#input file
cntrl=Controller(indatafile)
cntrl.readFile()

sys.stdout.write("Crtical section test complited\n")
sys.stdout.write("================================================================================\n")
