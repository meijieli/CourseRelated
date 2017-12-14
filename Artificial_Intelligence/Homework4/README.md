# Neural Networks for Image Classification

Introduction
---

In this assignment we will use the Keras Neural Network API for Python to build neural networks for image classification. 

Data Set
---

We will work on the CIFAR-10 image data set described here: https://www.cs.toronto.edu/~kriz/cifar.html (Links to an external site.)Links to an external site.

The data set contains 60.000 images labeled with 10 different categories:

Numeric ID	Category Name
0	airplane
1	automobile
2	bird
3	cat
4	deer
5	dog
6	frog
7	horse
8	ship
9	truck

Each image is 32x32 pixels large and there are three color channels (red, green blue). Each image can therefore be represented as three 32x32 matrices or one 32x32x3 cube. 
Here are some example images: 

![image](https://github.com/Shenzhi-ZHANG/CourseRelated/blob/master/Artificial_Intelligence/Homework4/sample_img-1.png)

Files
---

* **classifier.py** - The image classifier implemented in two ways:
>>>>1) A simplistic neural network that just uses a hidden layer 
and a output layer. 
>>>>2) A convolutional neural network that uses filters to extract features from images, subsampling to reduce
the size of input, dropouts to prevent the model from over-fitting. 

For details, please see the comments added to the code.
