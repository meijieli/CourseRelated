import numpy as np
import tensorflow as tf
from keras.datasets import cifar10
from keras import Sequential
from keras.layers import Dense, Flatten, Conv2D, MaxPooling2D, Dropout
from keras import optimizers



#----------------------- Loading the CIFAR-10 Data -----------------------#
def load_cifar10():
    '''
        Load the cifar-10 data and return 4 numpy arrays xtrain, ytrain_1hot,
        xtest, ytest_1hot.
    '''
    
    train, test = cifar10.load_data()               # load data
    xtrain, ytrain = train                          # get training data
    xtest, ytest = test                             # get test data

    # Build 1-hot representation for ytrain
    len_train = len(ytrain)
    ytrain_1hot = np.zeros((len_train, 10))         # all zeros
    train_index1 = np.arange(len_train)             # index of rows
    train_index2 = ytrain.reshape(1, len_train)     # index of columns
    ytrain_1hot[train_index1, train_index2] = 1     # fancy index

    # Build 1-hot representation for ytest
    len_test = len(ytest)
    ytest_1hot = np.zeros((len_test, 10))
    test_index1 = np.arange(len_test)
    test_index2 = ytest.reshape(1, len_test)
    ytest_1hot[test_index1, test_index2] = 1

    # Normalization
    xtrain = xtrain / 255
    xtest = xtest / 255
    
    return xtrain, ytrain_1hot, xtest, ytest_1hot



#----------------------- Design Network -----------------------#
def build_multilayer_nn():
    '''
        Create and return a Keras model object.
        The result of nn.evaluate(): [1.4383924636840821, 0.49430000000000002]
    '''
    
    nn = Sequential()                                   # an instance of the class keras.Sequential
    
    nn.add(Flatten(input_shape=(32, 32, 3)))            # flatten the input
    
    # dense layer(all neurons are connected to all inputs) with 100 neurons and use rectifier function
    hidden = Dense(units=100, activation="relu")   
    nn.add(hidden)                                      # add hidden layer to the network

    # output layer contains 10 neurons. Softmax function makes the activations sum up to 1.0.
    # Therefore, we can think of the output activation as a probability distribution
    output = Dense(units = 10, activation = "softmax")
    nn.add(output)                                      # add output layer to the network

    return nn

    '''
    nn.summary()
        _____________________________________________________________
        Layer (type)                 Output Shape              Param #   
        =================================================================
        flatten_3 (Flatten)          (None, 3072)              0         
        _________________________________________________________________
        dense_1 (Dense)              (None, 100)               307300    
        _________________________________________________________________
        dense_2 (Dense)              (None, 10)                1010      
        =================================================================
        Total params: 308,310
        Trainable params: 308,310
        Non-trainable params: 0
        _________________________________________________________________
    '''



#----------------------- Train Model -----------------------#
def train_multilayer_nn(model, xtrain, ytrain_1hot):
    '''
        Train the neural network.
        
        optimizer: stochastic gradient descent, learning rate 0.01
        loss function: categorical_crossentropy (measures how different the output distribution
        is from the target distribution (i.e. the 1-hot target vector))
        batch size = 32, train for 20 epochs
    '''

    sgd = optimizers.SGD(lr=0.01)
    model.compile(loss='categorical_crossentropy', optimizer=sgd, metrics=['accuracy']) 
    model.fit(xtrain, ytrain_1hot, epochs=20, batch_size=32)        
 


#------------------ Build Convolutional Neural Network ------------------#
def build_convolution_nn():
    '''
        Build convolutional neural network consists of convolution layers and pooling layers
        for feature extraction and dense layers for classification.
        Original result:
            [0.79482001466751095, 0.72240000000000004]
        Improved result:
            [0.72302089271545411, 0.74850000000000005]
    '''
    nn = Sequential()
    # first convolutional layer, input_shape: 32x32x3, filter size: 3x3, feature map: 32
    nn.add(Conv2D(32, (3, 3), activation = 'relu', padding = 'same', input_shape = (32, 32, 3)))

    # second convolutional layer
    nn.add(Conv2D(32, (3, 3), activation = 'relu', padding = 'same'))

    # a pooling layer
    nn.add(MaxPooling2D(pool_size = (2, 2)))

    # a drop-out layer, prevent from over-fitting
    nn.add(Dropout(0.25))

    # two more convolution layers
    nn.add(Conv2D(32, (3, 3), activation = 'relu', padding = 'same'))
    nn.add(Conv2D(32, (3, 3), activation = 'relu', padding = 'same'))

    # one more pooling layer
    nn.add(MaxPooling2D(pool_size = (2, 2)))

    # one more drop-out layer, prevent from over-fitting
    nn.add(Dropout(0.5))

    # flatten before feeding into dense layers
    nn.add(Flatten())
    
    # two dense layers
    nn.add(Dense(units=250, activation="relu"))
    nn.add(Dense(units=100, activation="relu"))

    # output layer
    nn.add(Dense(units=10, activation="softmax"))
    
    return nn

    '''
    nn.summary()
        _________________________________________________________________
        Layer (type)                 Output Shape              Param #   
        =================================================================
        conv2d_1 (Conv2D)            (None, 32, 32, 32)        896       
        _________________________________________________________________
        conv2d_2 (Conv2D)            (None, 32, 32, 32)        9248      
        _________________________________________________________________
        max_pooling2d_1 (MaxPooling2 (None, 16, 16, 32)        0         
        _________________________________________________________________
        dropout_1 (Dropout)          (None, 16, 16, 32)        0         
        _________________________________________________________________
        conv2d_3 (Conv2D)            (None, 16, 16, 32)        9248      
        _________________________________________________________________
        conv2d_4 (Conv2D)            (None, 16, 16, 32)        9248      
        _________________________________________________________________
        max_pooling2d_2 (MaxPooling2 (None, 8, 8, 32)          0         
        _________________________________________________________________
        dropout_2 (Dropout)          (None, 8, 8, 32)          0         
        _________________________________________________________________
        flatten_2 (Flatten)          (None, 2048)              0         
        _________________________________________________________________
        dense_3 (Dense)              (None, 250)               512250    
        _________________________________________________________________
        dense_4 (Dense)              (None, 100)               25100     
        _________________________________________________________________
        dense_5 (Dense)              (None, 10)                1010      
        =================================================================
        Total params: 567,000
        Trainable params: 567,000
        Non-trainable params: 0
        _________________________________________________________________
    '''


#------------------ Train Convolutional Neural Network ------------------#
def train_convolution_nn(model, xtrain, ytrain_1hot):
    '''
        Train the convolutional neural network.
    '''

    sgd = optimizers.SGD(lr=0.02)
    model.compile(loss='categorical_crossentropy', optimizer=sgd, metrics=['accuracy'])
 ##   model.fit(xtrain, ytrain_1hot, epochs=20, batch_size=32)
    model.fit(xtrain, ytrain_1hot, epochs=20, batch_size=32)


    
#----------------------- Loading the CIFAR-10 Data -----------------------#
def get_binary_cifar10():    
    '''
        Load the cifar-10 data and return 4 numpy arrays xtrain, ytrain,
        xtest, ytest. Here, ytrain is a binary vector of size(50000,) and
        ytest should be a vector of size(10000,) where 1 indicates an animal
        and 0 indicates a vehicle.
    '''
    
    train, test = cifar10.load_data()               # load data
    xtrain, ytrain = train                          # get training data
    xtest, ytest = test                             # get test data

    animal_list = [2, 3, 4, 5, 6, 7]
    vehicle_list = [0, 1, 8, 9]

    # Process ytrain
    index = 0
    for value in ytrain:
        if value in animal_list:
            ytrain[index] = 1
        else:
            ytrain[index] = 0
        index = index + 1

    # Process ytest
    index = 0
    for value in ytest:
        if value in animal_list:
            ytest[index] = 1
        else:
            ytest[index] = 0
        index = index + 1

    # Normalization
    xtrain = xtrain / 255
    xtest = xtest / 255
    
    return xtrain, ytrain, xtest, ytest


#---------- Build Convolutional Network for Binary Classification----------#
def build_binary_classifier():    
    '''
        Build convolutional neural network consists of convolution layers and pooling layers
        for feature extraction and dense layers for classification.
        The result of .evaluate(): [0.15369295902252197, 0.93930000000000002]
    '''
    nn = Sequential()
    # first convolutional layer, input_shape: 32x32x3, filter size: 3x3, feature map: 32
    nn.add(Conv2D(32, (3, 3), activation = 'relu', padding = 'same', input_shape = (32, 32, 3)))

    # second convolutional layer
    nn.add(Conv2D(32, (3, 3), activation = 'relu', padding = 'same'))

    # a pooling layer
    nn.add(MaxPooling2D(pool_size = (2, 2)))

    # a drop-out layer, prevent from over-fitting
    nn.add(Dropout(0.25))

    # two more convolution layers
    nn.add(Conv2D(32, (3, 3), activation = 'relu', padding = 'same'))
    nn.add(Conv2D(32, (3, 3), activation = 'relu', padding = 'same'))

    # one more pooling layer
    nn.add(MaxPooling2D(pool_size = (2, 2)))

    # one more drop-out layer, prevent from over-fitting
    nn.add(Dropout(0.5))

    # flatten before feeding into dense layers
    nn.add(Flatten())
    
    # two dense layers
    nn.add(Dense(units=250, activation="relu"))
    nn.add(Dense(units=100, activation="relu"))

    # output layer
    nn.add(Dense(units=1, activation="sigmoid"))
    
    return nn



#------------------- Train Convolutional Neural Network -------------------#
def train_binary_classifier(model, xtrain, ytrain):
    '''
        Train the convolutional neural network.
    '''

    sgd = optimizers.SGD(lr=0.02)
    model.compile(loss='binary_crossentropy', optimizer=sgd, metrics=['accuracy'])
    model.fit(xtrain, ytrain, epochs=20, batch_size=32)



#----------------------- Main Function -----------------------#
if __name__ == "__main__":

##    xtrain, ytrain_1hot, xtest, ytest_1hot = load_cifar10()
##    nn_multi = build_multilayer_nn()
##    train_multilayer_nn(nn_multi, xtrain, ytrain_1hot)
##    nn_multi.evaluate(xtest, ytest_1hot)
    
##    xtrain, ytrain_1hot, xtest, ytest_1hot = load_cifar10()
##    nn_convo = build_convolution_nn()
##    train_convolution_nn(nn_convo, xtrain, ytrain_1hot)
##    nn_convo.evaluate(xtest, ytest_1hot)

    xtrain, ytrain, xtest, ytest = get_binary_cifar10()
    nn_binary = build_binary_classifier()
    train_binary_classifier(nn_binary, xtrain, ytrain)
    nn_binary.evaluate(xtest, ytest)
