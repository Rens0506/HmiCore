/*******************************************************************************
 * The MIT License (MIT)
 * Copyright (c) 2015 University of Twente
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *******************************************************************************/
package hmi.tts.util;

import java.util.Collection;
import java.util.Iterator;

import hmi.tts.Bookmark;
import hmi.tts.Phoneme;
import hmi.tts.TTSCallback;
import hmi.tts.WordDescription;

/**
 * Does callbacks based on word description and bookmark lists
 * Currently only runs phoneme, bookmark and wordboundary callbacks.
 * Phoneme feedback is given at the start of the phoneme.
 * TODO: mix with viseme callback if needed  
 * TODO: test this
 * @author Herwin
 */
public class FakeCallback
{
    private final Collection<WordDescription> wordDescriptions;
    private final Collection<Bookmark> bookmarks;
    private final TTSCallback callback;
    private final String speechContent;
    
    public FakeCallback(TTSCallback cb, Collection<Bookmark>bm, Collection<WordDescription> wds, String content)
    {
        callback = cb;
        bookmarks = bm;
        wordDescriptions = wds;
        speechContent = content;
    }
    
    private void doAllCallbacks(WordDescription wd, WordDescription wdNext)
    {
        callback.wordBoundryCallback(speechContent.indexOf(wd.getWord()), wd.getWord().length());
        for(Bookmark bm:bookmarks)
        {
            if(bm.getWord()==wd)
            {
                callback.bookmarkCallback(bm.getName());
            }
        }   
        
        Phoneme ph = null;
        if(wd.getPhonemes().size()>0)
        {
            Iterator<Phoneme> iter = wd.getPhonemes().iterator();
            ph = iter.next();
            while(iter.hasNext())
            {
                Phoneme phNext = iter.next();
                callback.phonemeCallback(ph.getNumber(), ph.getDuration(), phNext.getNumber(), ph.isStressed()); 
                ph = phNext;
            }
        }
        
        if(ph!=null)
        {
            if(wdNext!=null && wdNext.getPhonemes().size()>0)
            {
                Phoneme phNext = wdNext.getPhonemes().iterator().next();             
                callback.phonemeCallback(ph.getNumber(), ph.getDuration(), phNext.getNumber(), ph.isStressed());
            }
            else
            {
                //final phoneme, return to 0
                callback.phonemeCallback(ph.getNumber(), ph.getDuration(), 0, ph.isStressed());
            }
        }
    }
    
    private void doAllCallbacks(WordDescription wd, WordDescription wdNext, int offset, double start, double end)
    {
        if(start <= offset && offset < end)
        {
            callback.wordBoundryCallback(speechContent.indexOf(wd.getWord()), wd.getWord().length());
            for(Bookmark bm:bookmarks)
            {
                if(bm.getWord()==wd)
                {
                    callback.bookmarkCallback(bm.getName());
                }
            }   
        }
        
        Phoneme ph = null;
        if(wd.getPhonemes().size()>0)
        {
            Iterator<Phoneme> iter = wd.getPhonemes().iterator();
            ph = iter.next();
            while(iter.hasNext())
            {
                Phoneme phNext = iter.next();
                if(start <= offset && offset < end)
                {
                    callback.phonemeCallback(ph.getNumber(), ph.getDuration(), phNext.getNumber(), ph.isStressed());
                }
                else
                {
                    return;
                }
                offset += ph.getDuration();
                ph = phNext;
            }
        }
        
        //last phoneme in word, link to next word
        if(start <= offset && offset < end)
        {
            if(ph!=null)
            {            
                if(wdNext!=null && wdNext.getPhonemes().size()>0)
                {
                    Phoneme phNext = wdNext.getPhonemes().iterator().next();             
                    callback.phonemeCallback(ph.getNumber(), ph.getDuration(), phNext.getNumber(), ph.isStressed());
                }
                else
                {
                    //final phoneme, return to 0
                    callback.phonemeCallback(ph.getNumber(), ph.getDuration(), 0, ph.isStressed());
                }
            }
        }
    }
    
    /**
     * Runs all callbacks
     */
    public void callbackAll()
    {
        Iterator<WordDescription> iterWd = wordDescriptions.iterator();        
        WordDescription wd = null;
        if(wordDescriptions.size()>0)
        {
            wd = iterWd.next();
            
            while(iterWd.hasNext())
            {
                WordDescription wdNext = iterWd.next();
                doAllCallbacks(wd, wdNext);
                wd = wdNext;
            }
        }
        if(wd!=null)
        {
            //final callback
            doAllCallbacks(wd, null);
        }
        
        //end of sentence bookmarks
        for(Bookmark bm:bookmarks)
        {
            if(bm.getWord()==null)
            {
                callback.bookmarkCallback(bm.getName());
            }
        } 
    }
    
    /**
     * Runs all callbacks for which start &lt= time &lt end
     */
    public void callBack(double start, double end)
    {
        Iterator<WordDescription> iterWd = wordDescriptions.iterator();        
        WordDescription wd = null;
        int offset = 0;
        if(wordDescriptions.size()>0)
        {
            wd = iterWd.next();
            if(start <= offset+wd.getDuration() && offset < end)
            {
                while(iterWd.hasNext())
                {
                    WordDescription wdNext = iterWd.next();
                    doAllCallbacks(wd, wdNext,offset,start,end);
                    offset += wd.getDuration();
                    wd = wdNext;                
                }
            }
            else
            {
                return;
            }
        }
        if(wd!=null && start <= offset+wd.getDuration() && offset < end)
        {
            //final callback
            doAllCallbacks(wd, null,offset,start,end);
        }
        
        //end of sentence bookmarks
        for(Bookmark bm:bookmarks)
        {
            if(bm.getWord()==null && start <= offset+wd.getDuration() && offset+wd.getDuration()<end)
            {
                callback.bookmarkCallback(bm.getName());
            }
        } 
    }
}
