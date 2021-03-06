/** @group globals
  Defines a network flow constraint.
  
@param arc: a directed arc of the flow network. Arc \p i connects node \a arc[\p i,1] to node \a arc[\p i,2].
@param balance: the difference between input and output flow for each node.
@param flow: the flow going through each arc.
*/

predicate network_flow(array[int,1..2] of int: arc, 
	  	                 array[int] of int: balance,
                       array[int] of var int: flow) = 

    let { int: source_node = 1;
          int: sink_node = 2;
          set of int: ARCS = index_set_1of2(arc);
          set of int: NODES = index_set(balance);
    } in
  	assert (
	   ARCS == index_set(flow) /\
	   lb_array(arc) >= min(NODES) /\ 
	   ub_array(arc) <= max(NODES),
	   "network_flow: wrong sizes of input array parameters",

	let {
	  var int: cost;
	  array[index_set_1of2(arc)] of int: weight = [1 | i in index_set_1of2(arc)];
	  constraint cost >= 0;
	} in
	   jacop_networkflow(arc, flow, weight, balance, cost)
	);

/** @group globals
  Defines a network flow constraint with cost.
  
@param arc: a directed arc of the flow network. Arc \p i connects node \a arc[\p i,1] to node \a arc[\p i,2].
@param balance: the difference between input and output flow for each node.
@param weight: the unit cost of the flow through the arc.
@param flow: the flow going through each arc.
@param cost: the overall cost of the flow.
*/
predicate network_flow_cost(array[int,1..2] of int: arc, 
	  	      array[int] of int: balance,
	  	      array[int] of int: weight,
            array[int] of var int: flow, var int: cost) = 

    let { int: source_node = 1;
          int: sink_node = 2;
          set of int: ARCS = index_set_1of2(arc);
          set of int: NODES = index_set(balance);
    } in
  	assert (
	   ARCS == index_set(flow) /\
	   ARCS == index_set(weight) /\
	   lb_array(arc) >= min(NODES) /\ 
	   ub_array(arc) <= max(NODES),
	   "network_flow: wrong sizes of input array parameters",

	jacop_networkflow(arc, flow, weight, balance, cost)
	);

predicate jacop_networkflow(array[int, int] of int: arc, 
	  	            array[int] of var int: flow, array[int] of var int: weight,
	  	            array[int] of int: balance, var int: cost);

% predicate jacop_networkflow_reif(array[int, int] of int: arc, 
% 	  	            array[int] of var int: flow, array[int] of var int: weight,
% 	  	            array[int] of int: balance, var int: cost, var bool: b) =
%     abort("Reified networkflow/3 and networkflow_cost/5 are not supported.");

