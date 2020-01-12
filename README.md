# SNLPminiproject
SNLP mini project 2019-20
<p align="center"><img src = "https://github.com/shivam-bahedia/SNLPminiproject/blob/master/figure/FactChecker.png"/></p>

<h4>FactChecker :&nbsp;</h4>
<ul>
<li>Tokenization of the input facts.</li>
<li>Break the generated tokens in the form of Subject and Object by identifying the Predicate.</li>
<li>Assigning true/false value to the fact by searching the predicate on wiki pages of the generated token.</li>
</ul>
<h4><strong>LocalWiki :</strong></h4>
<ul>
<li>It fetches the result wiki pages of generated tokens.</li>
<li>It also stores the previously fetched data in the formal on text files in Local Store folder.</li>
</ul>
<p><strong>Input:&nbsp;<br /></strong>List of facts in natural language with their corresponding fact ids (train.tsv and test.tsv)</p>
<p><strong>Output:&nbsp;<br /></strong>Generates a triple file that maps the facts to its truth value (trainresult.ttl and testresult.ttl)</p>
<p>&nbsp;</p>
