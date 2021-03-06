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
package hmi.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Creates a Fifo List of fixed size. When new elements are added to a full list, the first elements are removed.
 *  
 * @author welberge
 */
public class CircularBuffer<E> implements List<E>
{
    private final ArrayList<E>buffer;
    private final int size;
    
    public CircularBuffer(int size)
    {
        buffer = new ArrayList<E>(size);
        this.size = size;
    }
    
    public E get(int index)
    {
        return buffer.get(index);
    }
    
    @Override
    public int size()
    {
        return buffer.size();
    }
    
    @Override
    public Iterator<E> iterator()
    {
        return buffer.iterator();
    }
    
    private void fixSize()
    {
        while(buffer.size()>size)
        {
            buffer.remove(0);
        }
    }
    
    @Override
    public boolean add(E element)
    {        
        if(buffer.add(element))
        {
            fixSize();
            return true;
        }
        return false;
    }
    
    @Override
    public void add(int index, E element)
    {
        buffer.add(index,element);
        fixSize();
    }
    
    @Override
    public boolean addAll(Collection<? extends E> elements)
    {
        if(buffer.addAll(elements))
        {
            fixSize();
            return true;
        }
        return false;
    }
    @Override
    public boolean addAll(int index, Collection<? extends E> elements)
    {
        if(buffer.addAll(index,elements))
        {
            fixSize();
            return true;
        }
        return false;
    }
    
    @Override
    public void clear()
    {
        buffer.clear();        
    }
    
    @Override
    public boolean contains(Object element)
    {
        return buffer.contains(element);
    }
    
    @Override
    public boolean containsAll(Collection<?> elements)
    {
        return buffer.containsAll(elements);
    }
    
    @Override
    public int indexOf(Object element)
    {
        return buffer.indexOf(element);
    }
    
    @Override
    public boolean isEmpty()
    {
        return buffer.isEmpty();
    }
    
    @Override
    public int lastIndexOf(Object element)
    {
        return buffer.lastIndexOf(element);
    }
    
    @Override
    public ListIterator<E> listIterator()
    {
        return buffer.listIterator();
    }
    @Override
    public ListIterator<E> listIterator(int index)
    {
        return buffer.listIterator(index);
    }
    
    @Override
    public boolean remove(Object element)
    {
        return buffer.remove(element);
    }
    @Override
    public E remove(int element)
    {
        return buffer.remove(element);
    }
    
    @Override
    public boolean removeAll(Collection<?> elements)
    {
        return buffer.removeAll(elements);        
    }
    @Override
    public boolean retainAll(Collection<?> elements)
    {
        return buffer.retainAll(elements);
    }
    @Override
    public E set(int index, E element)
    {
        return buffer.set(index, element);
    }
    @Override
    public List<E> subList(int index1, int index2)
    {
        return buffer.subList(index1,index2);
    }
    @Override
    public Object[] toArray()
    {
        return buffer.toArray();
    }
    @Override
    public <T> T[] toArray(T[] element)
    {
        return buffer.toArray(element);
    }
}
