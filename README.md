# SortingAlgorithms
A small program that visualises the most popular sorting algorithms!
## Table of contents
* [General info](#general-info)
* [Usage](#usage)
* [Inspiration](#inspiration)
* [Technologies](#technologies)
* [Features](#features)
* [Setup](#setup)
---
## General info
It's just a regular little programme demonstrating different algorithms of your choice. If you want to restart the programme at the end of one of the algorithms, click on restart. In the future I plan to add acceleration/deceleration, and a clearer demonstration of sorting
---
## Usage
There you can see how my programme works:

https://github.com/user-attachments/assets/3a141ba3-aadf-430b-a7f9-24010bb7cc47

---
## Inspiration
- Some insta guy
- My love to Stawska professor
- Idk, goth mommy
---
## Technologies
Project created with:
* Java Swing
* Java Awt
* Threads
---
## Features
I decided one day to write a visualisation of sorting algorithms, and here is my brainchild!
I have two files, Main.java and Sort.java, in Sort.java I tested different algorithms, and in Main.java I assembled them and added graphics and sounds. 
I used Swing and ABT of course, also initially I initialised main and auxiliary arrays to store our numbers. 

Then a function to start a new thread for a particular array. A function that updates the array every 50 milliseconds. 
To draw the array, we just go through the whole array, if we are now at a certain index, we colour the number in a different colour:
```
@Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        g.setColor(Color.WHITE);

        int width = 5;
        for (int i = 0; i < array.size(); i++)
        {
            int height = array.get(i) * 10;

            if (i == currentIndex)
            {
                g.setColor(Color.RED);
            }
            else if (i == minIndex || i == helper_for_shell)
            {
                g.setColor(Color.BLUE);
            }
            else
            {
                g.setColor(Color.WHITE);
            }

            g.fillRect(10 + i * (width + 5), 510 - height, width, height);
        }
    }

```

A function to play sound. And then a not the best and not the prettiest enumeration of all possible sorting algorithms. 
Everything is banally simple, if we rearrange some elements - we play a sound, if we are on a number - it is highlighted in a different colour.

In main() we create an array, then we shuffle it, saving it into auxiliary arrays. Then we simply create captions, captions and buttons, through which the user chooses an algorithm for visualisation. 

Then again there is not the best realisation of the array selection and its brief description, it works in such a way that the pressed button changes the variable which we then shove into switch and select the array. 
That's all :3 

---
## Setup
Just download latest release, if you can't idk
