# covid-19-search

An API made to index / search  and analyse Coronavirus related papers.
The papers are published by this [Kaggle challenge](https://www.kaggle.com/allen-institute-for-ai/CORD-19-research-challenge)

## Requirement
You need to download papers from Kaggle challenge page.

This project uses Elasticsearch 7, you can use the docker container using the docker-compose file in `containers`:
```
docker-compose -f containers/docker-compose.yml up
```

## Usage

Properly set Elasticsearch configuration in `resources/config.edn`.

a command is available to load papers
```
lein run -m covid-19-search.import --paper-dir resources/CORD-19-research-challenge
```

launch the API
``` 
lein ring server
```

# API

### search papers

```
GET /covid-19?query=coronavirus&limit=5
```
returns a list of short paper description.
``` javascript
{
  "data": [
    {
      "paper_id": "136eb8ca0c1420b21582827d1aaf78a8cd7082f4",
      "abstract": "Since 2010, a new variant of PEDV belonging to Genogroup 2 has been transmitting in China and further spreading to the Unites States and other Asian countries including Taiwan. In order to characterize in detail the temporal and geographic relationships among PEDV strains, the present study systematically evaluated the evolutionary patterns and phylogenetic resolution in each gene of the whole PEDV genome in order to determine which regions provided the maximal interpretative power. The result was further applied to identify the origin of PEDV that caused the 2014 epidemic in Taiwan. Thirty-four full genome sequences were downloaded from GenBank and divided into three non-mutually exclusive groups, namely, worldwide, Genogroup 2 and China, to cover different ranges of secular and spatial trends. Each dataset was then divided into different alignments by different genes for likelihood mapping and phylogenetic analysis. Our study suggested that both nsp3 and S genes contained the highest phylogenetic signal with substitution rate and phylogenetic topology similar to those obtained from the complete genome. Furthermore, the proportion of nodes with high posterior support (posterior probability N 0.8) was similar between nsp3 and S genes. The nsp3 gene sequences from three clinical samples of swine with PEDV infections were aligned with other strains available from GenBank and the results suggested that the virus responsible for the 2014 PEDV outbreak in Taiwan clustered together with Clade I from the US within Genogroup 2. In conclusion, the current study identified the nsp3 gene as an alternative marker for a rapid and unequivocal classification of the circulating PEDV strains which provides complementary information to the S gene in identifying the emergence of epidemic strain resulting from recombination.",
      "title": "Evolutionary characterization of the emerging porcine epidemic diarrhea virus worldwide and 2014 epidemic in Taiwan",
      "authors": [
        "Ming-Hua Sung. Taiwan. National Chung Hsing University.",
        "Ming-Chung Deng. Taiwan. Animal Health Research Institute.",
        "Yi-Hsuan Chung.",
        "Yu-Liang Huang. Taiwan. Animal Health Research Institute.",
        "Chia-Yi Chang. Taiwan. Animal Health Research Institute.",
        "Yu-Ching Lan. Taiwan. China Medical University.",
        "Hsin-Lin Chou. Taiwan. China Medical University.",
        "Day-Yu Chao. Taiwan. National Chung Hsing University."
      ]
    },
    {
      "paper_id": "48b2fe99a7c2b7b7524ff7d72b2867e8f21601e7",
      "abstract": "Using an established nonhuman primate model for H5N1 highly pathogenic influenza virus infection in humans, we have been able to demonstrate the prophylactic mitigation of the pulmonary damage characteristic of human fatal cases from primary influenza virus pneumonia with a low dose oral formulation of a commercially available parenteral natural human interferon alpha (Alferon N Injection Ò ). At the highest oral dose (62.5 IU/kg body weight) used there was a marked reduction in the alveolar inflammatory response with minor evidence of alveolar and interstitial edema in contrast to the hemorrhage and inflammatory response observed in the alveoli of control animals. The mitigation of severe damage to the lower pulmonary airway was observed without a parallel reduction in viral titers. Clinical trial data will be necessary to establish its prophylactic human efficacy for highly pathogenic influenza viruses.\nAbbreviations: HPAI-, highly pathogenic avian influenza; LPAI-, low pathogenic avian influenza; HPIVh, highly pathogenic influenza virus in humans; TLR, toll-like receptor; LDO, low dose oral; IFN, interferon.",
      "title": "Protection from pulmonary tissue damage associated with infection of cynomolgus macaques by highly pathogenic avian influenza virus (H5N1) by low dose natural human IFN-a administered to the buccal mucosa",
      "authors": [
        "David Strayer. United States. Hemispherx Biopharma, Inc.",
        "William Carter. United States. Hemispherx Biopharma, Inc.",
        "Bruce Stouch. United States. Newtown Square.",
        "Koert Stittelaar.",
        "Robert Thoolen.",
        "Albert Osterhaus.",
        "William Mitchell. United States. Vanderbilt University."
      ]
    },
    {
      "paper_id": "b3528add37d78281040babe78cffa1b58a9c5b62",
      "abstract": "publicly funded repositories, such as the WHO COVID database with rights for unrestricted research re-use and analyses in any form or by any means with acknowledgement of the original source. These permissions are granted for free by Elsevier for as long as the COVID-19 resource centre remains active. Article Mycobacterium tuberculosis Sulfolipid-1 Activates Nociceptive Neurons and Induces Cough Graphical Abstract Highlights d An Mtb organic extract activates nociceptive neurons and induces cough in guinea pigs d Mtb sulfolipid-1 is necessary and sufficient to trigger neuronal activation and cough d Guinea pigs infected with an SL-1-deficient Mtb mutant do not cough\nMycobacterium tuberculosis produces a glycolipid called sulfolipid-1 (SL-1) that triggers cough by activating nociceptive neurons.",
      "title": "In Brief Article Mycobacterium tuberculosis Sulfolipid-1 Activates Nociceptive Neurons and Induces Cough",
      "authors": [
        "Cody Ruhl. USA. University of Texas Southwestern Medical Center.",
        "Breanna Pasko. USA. University of Texas Southwestern Medical Center.",
        "Haaris Khan. USA. University of Texas Southwestern Medical Center.",
        "Gregory Dussor. USA. University of Texas at Dallas.",
        "Theodore Price. USA. University of Texas at Dallas.",
        "Michael Correspondence.",
        "Lexy Kindt. USA. University of Texas Southwestern Medical Center.",
        "Chelsea Stamm. USA. University of Texas Southwestern Medical Center.",
        "Luis Franco. USA. University of Texas Southwestern Medical Center.",
        "Connie Hsia. USA. University of Texas Southwestern Medical Center.",
        "Min Zhou. USA. University of Texas Southwestern Medical Center.",
        "Colton Davis. USA. University of Texas Southwestern Medical Center.",
        "Tian Qin. USA. University of Texas Southwestern Medical Center.",
        "Laurent Gautron. USA. University of Texas Southwestern Medical Center.",
        "Michael Burton. USA. University of Texas at Dallas.",
        "Galo Mejia. USA. University of Texas at Dallas.",
        "Dhananjay Naik. USA. University of Texas at Dallas.",
        "Michael Shiloh. USA. University of Texas Southwestern Medical Center. michael.shiloh@utsouthwestern.edu."
      ]
    }
  ],
  "paging": {
    "total-hits": 10000,
    "next": {
      "limit": 3,
      "offset": 3
    }
  }
}
```

3 paper formats are available: `abstract` (default), `full-text`, `raw-doc`.


### Get paper from id
Having a paper id you can directly retrieve it, with the desired format:
```
GET /covid-19/136eb8ca0c1420b21582827d1aaf78a8cd7082f4?format=raw-doc
```

### Related terms
This API proposes to retrieve terms than are highly correlated with a given query
For instance:
```
GET /covid-19/related?query=coronavirus&limit=15
```
Returns top 15 terms most correlated to `coronavirus` query. The result are ordered by similarity score. If you look at following result you can notice terms referring to different coronavirus type aronyms (`cov`, `hcov`, `bcov`), and also other human coronavirus referenced like `mhv`, `229e`, `oc43`, `nl63` and `mers`:
``` javascript
[
  {
    "word": "hcov",
    "similarity": 0.9505666301889545,
    "paper_count": 222
  },
  {
    "word": "cov",
    "similarity": 0.9203295088672175,
    "paper_count": 505
  },
  {
    "word": "mhv",
    "similarity": 0.8581913276166149,
    "paper_count": 220
  },
  {
    "word": "229e",
    "similarity": 0.8537065639659747,
    "paper_count": 251
  },
  {
    "word": "coronaviridae",
    "similarity": 0.8183307295409377,
    "paper_count": 236
  },
  {
    "word": "spike",
    "similarity": 0.7965970220369267,
    "paper_count": 324
  },
  {
    "word": "covs",
    "similarity": 0.781496317490595,
    "paper_count": 155
  },
  {
    "word": "oc43",
    "similarity": 0.6210263444831345,
    "paper_count": 200
  },
  {
    "word": "bcov",
    "similarity": 0.590454361576188,
    "paper_count": 82
  },
  {
    "word": "betacoronavirus",
    "similarity": 0.5783345454545454,
    "paper_count": 117
  },
  {
    "word": "coronaviral",
    "similarity": 0.5642348726560313,
    "paper_count": 84
  },
  {
    "word": "nl63",
    "similarity": 0.560530395884214,
    "paper_count": 173
  },
  {
    "word": "tgev",
    "similarity": 0.5381342319435509,
    "paper_count": 152
  },
  {
    "word": "bats",
    "similarity": 0.5374806702664333,
    "paper_count": 200
  },
  {
    "word": "mers",
    "similarity": 0.5264057258234638,
    "paper_count": 291
  }
]
```

The next example retrieves the the most correlated terms to the query `coronavirus chloroquine` which would match all the papers that contain both terms, enabling us to discover other experimented treatments:
```
GET /covid-19/related?query=coronavirus%20chloroquine&limit=10
```
returns:
``` javascript
[
  {
    "word": "antimalarial",
    "similarity": 5.633202149135253,
    "paper_count": 106
  },
  {
    "word": "hydroxychloroquine",
    "similarity": 4.4404535113770285,
    "paper_count": 40
  },
  {
    "word": "lysosomotropic",
    "similarity": 3.993363238573819,
    "paper_count": 52
  },
  {
    "word": "aminoquinoline",
    "similarity": 3.146703626858017,
    "paper_count": 29
  },
  {
    "word": "mefloquine",
    "similarity": 2.995022428930365,
    "paper_count": 39
  },
  {
    "word": "amodiaquine",
    "similarity": 2.5415520963418494,
    "paper_count": 27
  },
  {
    "word": "keyaerts",
    "similarity": 2.3106099413697216,
    "paper_count": 24
  },
  {
    "word": "remdesivir",
    "similarity": 2.0830190751127713,
    "paper_count": 35
  },
  {
    "word": "savarino",
    "similarity": 2.0663657454381914,
    "paper_count": 16
  },
  {
    "word": "acidification",
    "similarity": 2.055765919562034,
    "paper_count": 89
  },
  {
    "word": "endosomal",
    "similarity": 1.9564042852375485,
    "paper_count": 134
  },
  {
    "word": "chlorpromazine",
    "similarity": 1.8268704818493298,
    "paper_count": 33
  },
  {
    "word": "endosomes",
    "similarity": 1.6897382362022824,
    "paper_count": 109
  },
  {
    "word": "quinine",
    "similarity": 1.6179844393842167,
    "paper_count": 37
  },
  {
    "word": "cq",
    "similarity": 1.5927437069634496,
    "paper_count": 45
  }
]
```

You can also look for `risk factor` and discover that `smoking`, `age`, `gender` and `chonic` deceases are `variables` that impacts the severity of coronaviruses:
``` javascript
[
  {
    "word": "odds",
    "similarity": 0.8955792380193816,
    "paper_count": 228
  },
  {
    "word": "cohort",
    "similarity": 0.7768249060654856,
    "paper_count": 294
  },
  {
    "word": "associations",
    "similarity": 0.5774739794943001,
    "paper_count": 236
  },
  {
    "word": "smoking",
    "similarity": 0.5668480495073148,
    "paper_count": 154
  },
  {
    "word": "logistic",
    "similarity": 0.5148002385211689,
    "paper_count": 180
  },
  {
    "word": "variables",
    "similarity": 0.50372242624997,
    "paper_count": 330
  },
  {
    "word": "prospective",
    "similarity": 0.49321933823354497,
    "paper_count": 229
  },
  {
    "word": "association",
    "similarity": 0.47625198693012194,
    "paper_count": 515
  },
  {
    "word": "age",
    "similarity": 0.4720340822774744,
    "paper_count": 584
  },
  {
    "word": "ci",
    "similarity": 0.45021655140284494,
    "paper_count": 245
  },
  {
    "word": "chronic",
    "similarity": 0.4470109242902746,
    "paper_count": 452
  },
  {
    "word": "sigurs",
    "similarity": 0.41273894500561165,
    "paper_count": 16
  },
  {
    "word": "prevalence",
    "similarity": 0.4025656295683987,
    "paper_count": 379
  },
  {
    "word": "incidence",
    "similarity": 0.40132008277525216,
    "paper_count": 393
  },
  {
    "word": "regression",
    "similarity": 0.39895947564126805,
    "paper_count": 255
  },
  {
    "word": "outcomes",
    "similarity": 0.3977056886227545,
    "paper_count": 333
  },
  {
    "word": "status",
    "similarity": 0.38565297567778184,
    "paper_count": 415
  },
  {
    "word": "outcome",
    "similarity": 0.3766494775477232,
    "paper_count": 366
  },
  {
    "word": "predictors",
    "similarity": 0.37657487553858865,
    "paper_count": 124
  },
  {
    "word": "gender",
    "similarity": 0.36120545868081877,
    "paper_count": 135
  }
]
```

## License

Copyright © 2020 Guillaume ERETEO

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
