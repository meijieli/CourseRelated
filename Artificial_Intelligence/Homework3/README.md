# Naive Bayes classifier

Introduction
---

In this assignment we will implement a Naive Bayes classifier and use it for spam filtering on a text message data set. 

Data Set:
---

The data set we will train and evaluate our classifier on contains 5574 SMS text messages, 747 of which are spam. We will use three files, train.txtPreview the documentView in a new window, dev.txtPreview the documentView in a new window, and test.txtPreview the documentView in a new window containing training, development, and testing data respectively. 

The raw data was downloaded from http://dcomp.sor.ufscar.br/talmeida/smspamcollection/ (Links to an external site.)Links to an external site.. There is also a paper describing the data set:

Almeida, T.A., Gómez Hidalgo, J.M., Yamakami, A. "Contributions to the Study of SMS Spam Filtering: New Collection and Results".  Proceedings of the 2011 ACM Symposium on Document Engineering (DOCENG'11).

Data File Format:
---

Each line in a data file represents one input/output sample. A line contains two fields, separated by a tabstop ("\t"). The first field contains the output label, "spam" or "ham". The second field contains the actual message text. For example  

```
spam	Congratulations ur awarded either £500 of CD gift vouchers & Free entry 2 our £100 weekly draw txt MUSIC to 87066 TnCs www.Ldew.com 1 win150ppmx3age16
ham	Just hopeing that wasn‘t too pissed up to remember and has gone off to his sisters or something!
```

Files:
---
**train.txt**: contains the training data for 5574 SMS text messages

**dev.txt**: contains the validation data

**test.txt**: contains the test data

**classifier.py**: the naive bayes classifier written in python, for detailed description, please refer to the comments in this file

Sample Output:
---

The program will print (a tuple of) four values: precision for predicting spam, recall for predicting spam, fscore and classificaition accuracy. 
Execute the following command:
```python
python3 homework3.py train.txt test.txt
```
The output will be:
```
Precision:0.9491525423728814 Recall:0.8888888888888888 F-Score:0.9180327868852458 Accuracy:0.9820466786355476
```
