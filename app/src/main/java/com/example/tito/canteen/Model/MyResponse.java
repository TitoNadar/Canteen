package com.example.tito.canteen.Model;

import java.util.List;

/**
 * Created by tito on 12/11/17.
 */

public class MyResponse  {
    public long multicast_id;
    public int success;
    public int failure;
    public int canonical_ids;
    public List<Result> results;
}
