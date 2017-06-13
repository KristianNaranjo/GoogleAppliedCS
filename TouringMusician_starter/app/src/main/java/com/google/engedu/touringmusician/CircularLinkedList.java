/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;


import android.graphics.Point;
import android.util.Log;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;

        private Node(Point p){
            prev = null;
            next = null;
            point = p;
        }
    }

    Node head;

    public void insertBeginning(Point p) {

        if(head == null){
            Node newNode = new Node(p);
            newNode.next = newNode;
            newNode.prev = newNode;
            head = newNode;
        }
        else {
            Node newNode = new Node(p);
            newNode.next = head;
            newNode.prev = head.prev;
            newNode.prev.next = newNode;
            head = newNode;
        }
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;
        CircularLinkedListIterator iterator = new CircularLinkedListIterator();
        Point curr = iterator.next();
        while(iterator.hasNext()){
            Point next = iterator.next();
            total += distanceBetween(curr, next);
            curr = next;
        }
        return total;
    }

    public void insertNearest(Point p) {
        if(head == null || head.next == head){
            insertBeginning(p);
        }
        else{
            Node minNode = getNearestNode(p);
            Node newNode = new Node(p);
            newNode.next = minNode.next;
            minNode.next.prev = newNode;
            minNode.next = newNode;
            newNode.prev = minNode;

        }
    }

    public Node getNearestNode(Point p){
        CircularLinkedListIterator iterator = new CircularLinkedListIterator();
        Node curr = iterator.nextNode();
        Node minNode = null;
        float minDistance = 0;

        if(!iterator.hasNext())
            return curr;

        while(iterator.hasNext()){
            if(minDistance == 0){
                minDistance = distanceBetween(curr.point, p);
                minNode = curr;
            }
            float newDistance = distanceBetween(curr.point, p);
            if(newDistance < minDistance && minDistance != 0) {
                minDistance = newDistance;
                minNode = curr;
            }
            curr = iterator.nextNode();
        }
        return minNode;
    }

    public void insertSmallest(Point p) {
        if(head == null || head.next == head){
            insertBeginning(p);
        }
        else{
            Node minNode = getNodeWithSmallestChange(p);
            Node newNode = new Node(p);
            newNode.next = minNode.next;
            minNode.next.prev = newNode;
            minNode.next = newNode;
            newNode.prev = minNode;

        }
    }

    public Node getNodeWithSmallestChange(Point p){
        CircularLinkedListIterator iterator = new CircularLinkedListIterator();
        Node curr = iterator.nextNode();
        float distWithPoint;
        float minDifference = 0;
        Node returnNode = null;

        while(iterator.hasNext()){
            Node next = iterator.nextNode();
            float lineDistance = distanceBetween(curr.point, next.point);
            distWithPoint = distanceBetween(curr.point,p) + distanceBetween(p,next.point);
            float newDifference = distWithPoint - lineDistance;
            if(minDifference == 0){
                minDifference = newDifference;
                returnNode = curr;
            }
            if(newDifference < minDifference){
                minDifference = newDifference;
                returnNode = curr;
            }
            curr = next;
        }
        return returnNode;
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        public Node nextNode() {
            Node toReturn = current;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
