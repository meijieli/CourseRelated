"""
COMS W4701 Artificial Intelligence - Programming Homework 3

Naive Bayes Spam Classifier

@author: Shenzhi Zhang   sz2695
    
"""


import sys
import string
import codecs
import math




def extract_words(text):
    '''
    Pre-processing the text.
    Read in the text of an SMS message and return a list
    of individual words (strings).
    '''
    table = str.maketrans({key: None for key in string.punctuation}) # should be set as a global variable
    text = text.lower()                # to lowercase
    text = text.translate(table)       # delete all punctuations
    return text.split()         # return a list of words in the data sample   




class NbClassifier(object):
    '''
    Naive Bayes Spam Classifier
    '''

    def __init__(self, training_filename, stopword_file = None):
        self.attribute_types = set()
        self.label_prior = {}    
        self.word_given_label = {}
        self.stopwords = set()             # stopwords set, if stopword_file = None, set will be empty

        if stopword_file != None:          # read stopword_file or not
            self.ini_stopwords(stopword_file)
        self.collect_attribute_types(training_filename, 2)  # k
        self.train(training_filename)          




    def ini_stopwords(self, stopword_file):
        '''
        The function that initializes stopwords set
        '''
        file = codecs.open(stopword_file, 'r', 'UTF-8')
        for word in file:
            word = word.split('\n')
            self.stopwords.add(word[0])


    
     
    def collect_attribute_types(self, training_filename, k):
        '''
        Compute a vocabulary consisting of the set of unique words
        occurring at least k times in the training data.
        '''
        
        file = codecs.open(training_filename,'r','UTF-8')   # deal with non asc character
        counter = {}
        for line in file:
            line = line.strip()             # delete leading and trailing spaces
            line = line.split('\t', 1)[1]   # escape 'spam' and 'ham'
            l = extract_words(line)         # extract words from this line
            for word in l:
                if word not in self.stopwords:     # check stopwords
                    if word in counter:
                        counter[word] = counter[word] + 1
                    else:
                        counter[word] = 1
        for word in counter:
            if counter[word] >= k:                 # appears at least k times
                self.attribute_types.add(word)     # add to attribute set




    def processLine(self, line, dic):
        '''
       A utility function that modularizes the process of input
        '''
        counter = 0
        for word in line:
            if word in self.attribute_types:
                counter = counter + 1
                if word in dic:
                    dic[word] = dic[word] + 1
                else:
                    dic[word] = 1
        return counter




    def train(self, training_filename):
        '''
        Estimate two probability distributions from the training data
        '''
        # initialize variables
        spam_counter = 0
        ham_counter = 0
        spam_dic = {}
        ham_dic = {}
        total_spam_words = 0
        total_ham_words = 0
        c = 0.001   # parameter of Laplacian Smoothing
        file = codecs.open(training_filename,'r','UTF-8')   # deal with non asc character       

        # read in the file and count words
        for line in file:
            line = line.strip()
            [label, line] = line.split('\t',1)
            l = extract_words(line)
            # call utility function processLine
            if label == 'spam':
                spam_counter = spam_counter + 1
                total_spam_words = total_spam_words + self.processLine(l, spam_dic)
            else:
                ham_counter = ham_counter + 1
                total_ham_words = total_ham_words + self.processLine(l, ham_dic)

        # calculate prior probabilities for labels
        self.label_prior['spam'] = spam_counter / (spam_counter + ham_counter)
        self.label_prior['ham'] = ham_counter / (ham_counter + spam_counter)

        # calculate (smoothed) conditional probability for each attribute given the label
        # the parameter of Laplacian Smoothing is set to 1
        for word in self.attribute_types:
            if word not in spam_dic:
                spam_dic[word] = 0
            if word not in ham_dic:
                ham_dic[word] = 0
            self.word_given_label[(word,'spam')] = (spam_dic[word] + c) / (total_spam_words + c * len(self.attribute_types))
            self.word_given_label[(word,'ham')] = (ham_dic[word] + c) / (total_ham_words + c * len(self.attribute_types))
            

    

    def predict(self, text):
        '''
        Take the text of an SMS as input and compute
        the probability for each label given the words
        '''
        spam_pr = math.log(self.label_prior['spam'])        # default: prior probability
        ham_pr = math.log(self.label_prior['ham'])
        text = extract_words(text)
        for word in text:
            if word in self.attribute_types:
                # log probability of p(spam, word1, word2,..., wordn)
                spam_pr = math.fsum([spam_pr, math.log(self.word_given_label[(word,'spam')])])
                # log probability of p(ham, word1, word2,..., wordn)
                ham_pr = math.fsum([ham_pr, math.log(self.word_given_label[(word,'ham')])])
                
        return dict(spam = spam_pr, ham = ham_pr) 




    def evaluate(self, test_filename):
        '''
        Take in validation or test set as parameters and
        returns a tuple of four values:
        precision for predicting spam, recall for predicting spam,
        fscore, classification accuracy'''
        t_pos = 0        # predicted = 'spam' label = 'spam'
        f_pos = 0        # predicted = 'spam' label = 'ham'   
        t_neg = 0        # predicted = 'ham' label = 'ham'
        f_neg = 0        # predicted = 'ham' label = 'spam'
        file = codecs.open(test_filename,'r','UTF-8')   # deal with non asc character
        for line in file:
            line = line.strip()
            [label, line] = line.split('\t', 1)
            predicted = self.predict(line)
            if predicted['spam'] >= predicted['ham']:     # predicted as spam
                if label == 'spam':                  
                    t_pos = t_pos + 1
                else:
                    f_pos = f_pos + 1
            else:                                         # predicted as ham
                if label == 'spam':
                    f_neg = f_neg + 1
                else:
                    t_neg = t_neg + 1

        # calculate accuracy                   
        precision = t_pos / (t_pos + f_pos)
        recall = t_pos / (t_pos + f_neg)
        fscore = (2 * precision * recall) / (precision + recall)
        accuracy = (t_pos + t_neg) / (t_pos + f_pos + t_neg + f_neg)
        return precision, recall, fscore, accuracy


def print_result(result):
    print("Precision:{} Recall:{} F-Score:{} Accuracy:{}".format(*result))


if __name__ == "__main__":
    classifier = NbClassifier(sys.argv[1]) # without stopwords
#    classifier = NbClassifier(sys.argv[1],'stopwords_mini.txt') # with stopwords
    result = classifier.evaluate(sys.argv[2])
    print_result(result)
