

#include "html.h"
#include "gumbo/gumbo.h"
#include <cstring>



static bool isTextNode(GumboNode *node) { return node != nullptr && node->type == GUMBO_NODE_TEXT; }

static bool isElementNode(GumboNode *node) { return node != nullptr && node->type == GUMBO_NODE_ELEMENT; }


HtmlNode* HtmlNode::wrapNode(GumboNode *p)
{
	auto it = mNodeCache.find(p);
	if (it != mNodeCache.end()) {
		return it->second;
	}

	auto *n = new HtmlNode(p);
	mNodeCache[p] = n;
	return n;
}

HtmlNode::~HtmlNode()
{
	if (mNode == nullptr) {
		return;
	}

	mNode = nullptr;
	for (auto it : mNodeCache) {
		delete (it.second);
	}
}



const char* HtmlNode::attr(const char *name)
{
	if (! isElementNode(mNode)) {
		return nullptr;
	}
	auto attrs = &(mNode->v.element.attributes);
	auto attr = gumbo_get_attribute(attrs, name);

	return attr ? attr->value : nullptr;
}


int HtmlNode::attrs(::std::map<const char*, const char *>& map) 
{
	if (! isElementNode(mNode)) {
		return -1;
	}
	auto attrs = &(mNode->v.element.attributes);

	for (int i = 0; i < attrs->length; i++) {
    	auto attr = (GumboAttribute *) (attrs->data[i]);
    	map[attr->name] = attr->value;
	}
	return 0;
}

const char* HtmlNode::text()
{
	return isTextNode(mNode) ? mNode->v.text.text : nullptr;
}

const char* HtmlNode::tag()
{
	return isElementNode(mNode) ? gumbo_normalized_tagname(mNode->v.element.tag) : nullptr;
}

HtmlNode* HtmlNode::parent()
{
	return isEmpty() ? nullptr : wrapNode(mNode->parent);
}

template <typename T>
HtmlNode* HtmlNode::filter(::std::vector<HtmlNode*> *v, T const& func)
{
	if (! isElementNode(mNode)) {
		return emptyNode();
	}

	auto children = &(mNode->v.element.children);\
	auto firstNode = emptyNode();

	for (int i = 0; i < children->length; i++) {
		auto child = (GumboNode *) (children->data[i]);
		if (func(child)) {
			if (v == nullptr) {
				return wrapNode(child);
			}
			if (firstNode == emptyNode()) {
				firstNode = wrapNode(child);
			}
			v->push_back(wrapNode(child));
		}
	}
	return firstNode;
}


int HtmlNode::children(::std::vector<HtmlNode*>& v)
{
	if (! isElementNode(mNode)) {
		return -1;
	}
	filter(&v, [](GumboNode *node) -> bool { return true; });
	return 0;
}

int HtmlNode::childCount()
{
	return isElementNode(mNode) ? mNode->v.element.children.length : -1;
}

HtmlNode* HtmlNode::childAt(int index)
{
	return index < 0 || index >= childCount() ? emptyNode() : 
		wrapNode((GumboNode*)(mNode->v.element.children.data[index]));
}


HtmlNode* HtmlNode::getNodeByTag(::std::vector<HtmlNode*> *v,
		const char *tag)
{
	GumboTag gumboTag = gumbo_tag_enum(tag);
	return filter(v, [gumboTag](GumboNode *node) -> bool {
		return isElementNode(node) && node->v.element.tag == gumboTag;
	});
}


HtmlNode* HtmlNode::getNodeByAttr(::std::vector<HtmlNode*> *v,
		const char *name, const char *value)
{
	return filter(v, [name, value](GumboNode *p) -> bool {
		if (! isElementNode(p)) return false;
		auto attr = gumbo_get_attribute(&p->v.element.attributes, name);
		return attr != nullptr && strcmp(attr->value, value) == 0;
	});
}


HtmlNode* HtmlNode::getNodeByAttrStarting(::std::vector<HtmlNode*> *v,
		const char *name, const char *valuePrefix)
{
	return filter(v, [name, valuePrefix](GumboNode *p) -> bool {
		if (! isElementNode(p)) return false;
		auto attr = gumbo_get_attribute(&p->v.element.attributes, name);
		return attr != nullptr && strcmp(attr->value, valuePrefix) >= 0;
	});
}


HtmlNode* HtmlNode::getNodeByAttrEnding(::std::vector<HtmlNode*> *v,
		const char *name, const char *valueSuffix)
{
	size_t n = strlen(valueSuffix);

	return filter(v, [name, valueSuffix, n](GumboNode *p) -> bool {
		if (! isElementNode(p)) return false;
		auto attr = gumbo_get_attribute(&p->v.element.attributes, name);
		size_t nn;
		return attr != nullptr && (nn = strlen(attr->value)) >= n
				&& memcmp(attr->value + nn - n, valueSuffix, n) == 0;
	});
}

HtmlNode* HtmlNode::getNodeByAttrContaining(::std::vector<HtmlNode*> *v,
											   const char *name, const char *value)
{
	return filter(v, [name, value](GumboNode *p) -> bool {
		if (! isElementNode(p)) return false;
		auto attr = gumbo_get_attribute(&p->v.element.attributes, name);
		return attr != nullptr && strstr(attr->value, value) != nullptr;
	});
}


HtmlTree::HtmlTree(const char *buff)
{
	mTree = gumbo_parse_with_options(&kGumboDefaultOptions, buff, strlen(buff));
	mNode = mTree ? mTree->root : nullptr;
}

HtmlTree::~HtmlTree()
{
	if (mTree == nullptr) {
		return;
	}

	gumbo_destroy_output(&kGumboDefaultOptions, mTree);

	mTree = nullptr;
}