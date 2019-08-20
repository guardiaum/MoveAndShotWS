#Path Configuration

#Insert the root folder path of your caffe installation on next line;
#This step is very important because this path will prepend almost every other paths
CAFFE_ROOT = '/home/thiago/bin/caffe-rc5'
DEFAULT_RESULT_TXT_FILE = '/home/thiago/tcc-output.txt' # Txt file path for output
SYNSET_WORDS_PATH = './synset_words-ptbr.txt' #Synset words

def importCaffe():
    sys.path.insert(0, CAFFE_ROOT+'/python')

import sys
importCaffe()


import numpy as np
import matplotlib.pyplot as plt
import caffe
import webbrowser
import os
from PIL import Image
from PIL import ImageFont
from PIL import ImageDraw
import time #For execution time measurement



def configureTools():

    plt.rcParams['figure.figsize'] = (10,10)
    plt.rcParams['image.interpolation'] = 'nearest'
    plt.rcParams['image.cmap'] = 'gray'

def filepath():
    if len(sys.argv)  == 2:
        return sys.argv[1]
    else:
        print("Usage: python HybridIdentifier.py image_path")




#Setup Caffe and instantiates net
def setupCaffe():
    caffe.set_mode_cpu() #Change to caffe.set_mode_gpu() if there's any GPU available, it may speed things up

def getNet():
    #Those files can be found on the following link: https://onedrive.live.com/?authkey=%21AAFW2-FVoxeVRck&id=4006CBB8476FF777%2117887&cid=4006CBB8476FF777
    #Github repo: https://github.com/KaimingHe/deep-residual-networks#models
    model_def = CAFFE_ROOT + '/data/places/ResNet-152-deploy.prototxt'
    model_weights = CAFFE_ROOT + '/data/places/ResNet-152-model.caffemodel'
    net = caffe.Net(model_def,      # defines the structure of the model
                model_weights,  # contains the trained weights
                caffe.TEST)
    return net

def loadAndConfigureMean():
    #TODO I lost the mean file used below, but the binaryproto file in this folder can be converted to npy using caffe methods.
    #After a successfully conversion, just uncomment the lines below
    #More info at: https://github.com/BVLC/caffe/issues/290#issuecomment-62846228

    #mu = np.load('./hybridCNN_mean.npy')
    #mu = mu.mean(1).mean(1)
    #return mu

def createTransformer(net, mean):
    transformer = caffe.io.Transformer({'data': net.blobs['data'].data.shape})
    transformer.set_transpose('data', (2,0,1))
    transformer.set_mean('data', mean)
    transformer.set_raw_scale('data',255)
    transformer.set_channel_swap('data',(2,1,0))
    return transformer


def write_to_file(text,path):
     output_file = open(path, 'a')
     output_file.write(text)
     output_file.close

def transform_images(transformer):
    result_hash = {}
    image = caffe.io.load_image(filepath())
    transformed_image = transformer.preprocess('data',image)
    result_hash[filepath()] = transformed_image
    return result_hash

def predictClass(net, transformed_image):

    net.blobs['data'].reshape(1,3,224,224)  # image size is 227x227
    net.blobs['data'].data[...] = transformed_image

    output = net.forward()
    output_prob = output['prob'][0] # the output probability vector for the first image in the batch
    return output_prob

def outputClassLabel(output_prob, nCategory):
    labels = np.loadtxt(SYNSET_WORDS_PATH, str, delimiter='\t')
    top_inds = output_prob.argsort()[::-1][:nCategory]  # reverse sort and take five largest items
    return zip(output_prob[top_inds], labels[top_inds])

def openGoogleAndSearch(term):
    url = "https://www.google.com/search?q={}".format(term)
    webbrowser.open(url)

# Function used to stamp on the image the resulting prediction rank
def stamp(label, image_path, y):
    img = Image.open(image_path)
    draw = ImageDraw.Draw(img)
    font = ImageFont.truetype("LiberationMono-Bold.ttf", int(img.size[1]*0.1))
    draw.text((0, y*int(img.size[0]*0.025)),label,(140,140,140),font=font)
    img.save(image_path)

#Find synsets
def findSynsetWord(word):
    synsetWordsFile = open(SYNSET_WORDS_PATH, "r")
    while True:
        line = synsetWordsFile.readline()
        if not line: break
        if word in line: return line
    return -1

#Main execution
if __name__ == '__main__':

    main_init_time = time.time() #Starts execution time counter

    configureTools()
    importCaffe()
    setupCaffe()
    net = getNet()
    mean = loadAndConfigureMean()
    transformer = createTransformer(net, mean)
    transformed_images = transform_images(transformer)

    for image_path, image in transformed_images.iteritems():
        predictions_init_time = time.time() #Starts predictions time counter

        #Magic happens at next line =)
        output_prob=predictClass(net,image)
        labels = outputClassLabel(output_prob,5)

        #Logging
        write_to_file("ARQUIVO: " + image_path + "\n", DEFAULT_RESULT_TXT_FILE)

        categories = ""
        for i in range(0,len(labels)):
            probability = labels[i][0]
            word_code = labels[i][1].split()[0]
	    line = str(findSynsetWord(word_code))
	    word = line
	    write_to_file(word, DEFAULT_RESULT_TXT_FILE)
	    line = line.split(",")[0].replace('\n', ' ').replace('\r', '')
            categories = (categories + line+",") if i!=(len(labels)-1) else categories + line
        print categories

    end_time = time.time()
    main_execution_time = end_time - main_init_time
    predictions_execution_time = end_time - predictions_init_time
    total_execution_time = main_execution_time + predictions_execution_time
    print (total_execution_time)
