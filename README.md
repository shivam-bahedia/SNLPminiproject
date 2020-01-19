# SNLPminiproject - Team Blitz
<h2>Diagram</h2>
<p align="center">
  <img src = "https://github.com/shivam-bahedia/SNLPminiproject/blob/master/figure/FactChecker.png"/>
</p>

<h2>Module</h2>
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
<h2><strong>WorkFlow</strong></h2>
<ul>
<li><strong>Tokenization -&nbsp;</strong>Generate tokens for the fact, based on words starting with a capital letter including the&nbsp;prepositions following a noun.</li>
<li><strong>Data fetch from Wiki -&nbsp;</strong>For each generated token check if the data exists in Local Store, if not fetch data from Wikipedia and store it in the Local Store.</li>
<li><strong>Get predicate for the fact -&nbsp;</strong>For each fact generate a generic predicate based on the predefined list of predicates.</li>
<li><strong>Search Data -&nbsp;</strong>Search the data of each token for pattern containing the predicate and the other token.</li>
<li><strong>Assign Truth Value -&nbsp;</strong>If the pattern matches, assign true to the truth value.</li>
</ul>
<p><strong>Positive Example</strong></p>
<p>Fact: 3820514 Alfonso XIII of Spain's birth place is Madrid.</p>
<ol>
<li>Tokenization:<br /> {"Alfonso XIII of Spain" ,"Madrid "}</li>
<li>Data fetched from Wiki:<br />token1: {"Alfonso XIII of Spain"}&nbsp;<br />token2: {"Madrid"}</li>
<li>Get predicate for the fact:<br />(B|b)orn.{0,150}(?i)</li>
<li>Search Data:<br />String "Born (1886-05-17)17 May 1886 Royal Palace of Madrid" found in the wiki page of token1 ("Alfonso XIII of Spain")&nbsp;</li>
<li>return truthValue =true for this fact.</li>
</ol>
<p><strong>Negative Example</strong></p>
<p>Fact: 3885766 Lucille Ball's death place is Santa Monica, California.</p>
<ol>
<li>Tokenization:<br /> {"Lucille Ball" ,"Santa Monica, California "}</li>
<li>Data fetched from Wiki:<br />token1: {"Lucille Ball"}&nbsp;<br />token2: {"Santa Monica, California "}</li>
<li>Get predicate for the fact:<br />(Died).{0,150}(?i)</li>
<li>Search Data:<br />token2 not found in the wiki page of token1 with predicate<br />token1 not found in the wiki page of token2 with predicate</li>
<li>return truthValue =false for this fact.</li>
</ol>
<h2>Execution Steps</h2>
Directly execute the Main Class.
<h2><strong>Team Member&nbsp;</strong></h2>
<p>Shivam Bahedia, Sourabh Poddar, Yamini Punetha</p>
